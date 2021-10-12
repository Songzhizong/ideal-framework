/*
 * Copyright 2021 cn.idealframework
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.idealframework.cache.impl;

import cn.idealframework.cache.DistributedHashCache;
import cn.idealframework.cache.serialize.Deserializer;
import cn.idealframework.cache.serialize.Serializer;
import cn.idealframework.lock.DLock;
import cn.idealframework.lock.DLockFactory;
import cn.idealframework.util.Asserts;
import lombok.Setter;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author 宋志宗 on 2021/7/20
 */
@CommonsLog
@SuppressWarnings({"unchecked", "DuplicatedCode"})
public class RedisHashCache<V> implements DistributedHashCache<V> {
  @Setter
  private static String globalPrefix = "cache:";
  @Setter
  private static StringRedisTemplate redisTemplate;
  @Setter
  private static DLockFactory lockFactory;

  private final String prefix;
  @Nullable
  private final V defaultFallback;
  private final boolean randomTimeout;
  private final long timeoutSeconds;
  private final long minTimeoutSeconds;
  private final long maxTimeoutSeconds;
  private final Serializer<V> serializer;
  private final Deserializer<V> deserializer;

  public RedisHashCache(@Nonnull String namespace,
                        @Nullable V defaultFallback,
                        boolean randomTimeout,
                        long timeoutSeconds,
                        long minTimeoutSeconds,
                        long maxTimeoutSeconds,
                        @Nonnull Serializer<V> serializer,
                        @Nonnull Deserializer<V> deserializer) {
    this.prefix = globalPrefix + namespace;
    this.defaultFallback = defaultFallback;
    this.randomTimeout = randomTimeout;
    this.timeoutSeconds = Math.max(timeoutSeconds, 1L);
    this.minTimeoutSeconds = Math.max(minTimeoutSeconds, 1L);
    this.maxTimeoutSeconds = Math.max(maxTimeoutSeconds, 2L);
    this.serializer = serializer;
    this.deserializer = deserializer;
  }

  @Override
  public boolean hasKey(@Nonnull String key) {
    String redisKey = genRedisKey(key);
    Boolean hasKey = getRedisTemplate().hasKey(redisKey);
    return hasKey != null && hasKey;
  }

  @Override
  public boolean hasKey(@Nonnull String key, @Nonnull String hashKey) {
    String redisKey = genRedisKey(key);
    HashOperations<String, Object, Object> ops = getRedisTemplate().opsForHash();
    Boolean hasKey = ops.hasKey(redisKey, hashKey);
    //noinspection ConstantConditions
    return hasKey != null && hasKey;
  }

  @Nonnull
  @Override
  public V get(@Nonnull String key, @Nonnull String hashKey,
               @Nonnull Function<String, ? extends V> function, @Nonnull V fallback) {
    Asserts.notBlank(hashKey, "hashKey must be not blank");
    Asserts.nonnull(function, "function must be not null");
    Asserts.nonnull(fallback, "fallback must be not null");
    String redisKey = genRedisKey(key);
    StringRedisTemplate redisTemplate = getRedisTemplate();
    HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
    Object value = hash.get(redisKey, hashKey);
    if (value != null) {
      return deserializer.deserialize((String) value);
    }
    DLock lock = getLockFactory().getLock(redisKey + ":" + hashKey);
    try {
      lock.lock();
      value = hash.get(redisKey, hashKey);
      if (value != null) {
        return deserializer.deserialize((String) value);
      }
      V apply = function.apply(key);
      if (apply == null) {
        log.debug("function.apply(key) return null, use fallback");
        this.doPut(redisKey, hashKey, fallback);
        return fallback;
      }
      this.doPut(redisKey, hashKey, apply);
      return apply;
    } finally {
      lock.unlock();
    }
  }

  @Nonnull
  @Override
  public Map<String, V> getAllIfPresent(@Nonnull String key) {
    String redisKey = genRedisKey(key);
    StringRedisTemplate redisTemplate = getRedisTemplate();
    Map<Object, Object> entries = redisTemplate.opsForHash().entries(redisKey);
    Map<String, V> result = new HashMap<>();
    entries.forEach((k, v) -> {
      String hashKey = (String) k;
      String value = (String) v;
      V deserialize = deserializer.deserialize(value);
      result.put(hashKey, deserialize);
    });
    return result;
  }

  @Nullable
  @Override
  public V getIfPresent(@Nonnull String key, @Nonnull String hashKey) {
    Asserts.notBlank(hashKey, "hashKey must be not blank");
    String redisKey = genRedisKey(key);
    StringRedisTemplate redisTemplate = getRedisTemplate();
    Object value = redisTemplate.opsForHash().get(redisKey, hashKey);
    if (value == null) {
      return null;
    }
    return deserializer.deserialize((String) value);
  }

  @Nullable
  @Override
  public V get(@Nonnull String key, @Nonnull String hashKey, @Nonnull Function<String, ? extends V> function) {
    if (defaultFallback != null) {
      return get(key, hashKey, function, defaultFallback);
    }
    String redisKey = genRedisKey(key);
    StringRedisTemplate redisTemplate = getRedisTemplate();
    HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
    Object value = hash.get(redisKey, hashKey);
    if (value != null) {
      return deserializer.deserialize((String) value);
    }
    V apply = function.apply(key);
    if (apply != null) {
      this.doPut(redisKey, hashKey, apply);
    }
    return apply;
  }

  @Override
  public void put(@Nonnull String key, @Nonnull String hashKey, @Nonnull V value) {
    Asserts.notBlank(hashKey, "hashKey must be not blank");
    Asserts.nonnull(value, "value must be not null");
    String redisKey = genRedisKey(key);
    doPut(redisKey, hashKey, value);
  }

  @Override
  public void putAll(@Nonnull String key, @Nonnull Map<String, V> map) {
    Map<String, String> stringMap = new LinkedHashMap<>(map.size());
    map.forEach((k, v) -> stringMap.put(k, serializer.serialize(v)));
    String redisKey = genRedisKey(key);
    StringRedisTemplate redisTemplate = getRedisTemplate();
    Boolean hasKey = redisTemplate.hasKey(redisKey);
    if (hasKey != null && hasKey) {
      redisTemplate.opsForHash().putAll(redisKey, stringMap);
    } else {
      SessionCallback<Boolean> callback = new SessionCallback<Boolean>() {
        @Nonnull
        @Override
        public Boolean execute(@Nonnull RedisOperations operations) throws DataAccessException {
          operations.opsForHash().putAll(redisKey, stringMap);
          operations.expire(redisKey, getTimeoutSeconds(), TimeUnit.SECONDS);
          return true;
        }
      };
      redisTemplate.execute(callback);
    }
  }

  @Override
  public void setAll(@Nonnull String key, @Nonnull Map<String, V> map) {
    Map<String, String> stringMap = new LinkedHashMap<>(map.size());
    map.forEach((k, v) -> stringMap.put(k, serializer.serialize(v)));
    String redisKey = genRedisKey(key);
    StringRedisTemplate redisTemplate = getRedisTemplate();
    redisTemplate.delete(redisKey);
    SessionCallback<Boolean> callback = new SessionCallback<Boolean>() {
      @Nonnull
      @Override
      public Boolean execute(@Nonnull RedisOperations operations) throws DataAccessException {
        operations.opsForHash().putAll(redisKey, stringMap);
        operations.expire(redisKey, getTimeoutSeconds(), TimeUnit.SECONDS);
        return true;
      }
    };
    redisTemplate.execute(callback);
  }

  private void doPut(@Nonnull String redisKey, @Nonnull String hashKey, @Nonnull V value) {
    Asserts.notBlank(redisKey, "redisKey must be not blank");
    Asserts.notBlank(hashKey, "hashKey must be not blank");
    Asserts.nonnull(value, "value must be not null");
    String serialize = serializer.serialize(value);
    StringRedisTemplate redisTemplate = getRedisTemplate();
    Boolean hasKey = redisTemplate.hasKey(redisKey);
    if (hasKey != null && hasKey) {
      redisTemplate.opsForHash().put(redisKey, hashKey, serialize);
    } else {
      SessionCallback<Boolean> callback = new SessionCallback<Boolean>() {
        @Nonnull
        @Override
        public Boolean execute(@Nonnull RedisOperations operations) throws DataAccessException {
          operations.opsForHash().put(redisKey, hashKey, serialize);
          operations.expire(redisKey, getTimeoutSeconds(), TimeUnit.SECONDS);
          return true;
        }
      };
      redisTemplate.execute(callback);
    }
  }

  @Override
  public void invalidate(@Nonnull String key) {
    String redisKey = genRedisKey(key);
    getRedisTemplate().delete(redisKey);
  }

  @Override
  public void invalidate(@Nonnull String key, @Nonnull String hashKey) {
    Asserts.notBlank(hashKey, "hashKey must be not blank");
    String redisKey = genRedisKey(key);
    getRedisTemplate().opsForHash().delete(redisKey, hashKey);
  }

  @Override
  public void invalidateAll(@Nonnull Iterable<String> keys) {
    List<String> redisKeys = new ArrayList<>();
    for (String key : keys) {
      redisKeys.add(genRedisKey(key));
    }
    getRedisTemplate().delete(redisKeys);
  }

  @Nonnull
  private StringRedisTemplate getRedisTemplate() {
    if (RedisHashCache.redisTemplate == null) {
      log.error("RedisHashCache.redisTemplate为空, 请手动设置或配置ideal-boot-starter-redis");
      Asserts.nonnull(RedisHashCache.redisTemplate,
        "RedisHashCache.redisTemplate为空, 请手动设置或配置ideal-boot-starter-redis");
    }
    return RedisHashCache.redisTemplate;
  }

  @Nonnull
  private DLockFactory getLockFactory() {
    if (RedisHashCache.lockFactory == null) {
      log.error("RedisHashCache.lockFactory为空, 请手动设置或配置ideal-boot-starter-lock");
      Asserts.nonnull(RedisHashCache.lockFactory,
        "RedisHashCache.lockFactory为空, 请手动设置或配置ideal-boot-starter-lock");
    }
    return RedisHashCache.lockFactory;
  }


  @Nonnull
  private String genRedisKey(@Nonnull String key) {
    Asserts.notBlank(key, "Key must be not blank");
    return prefix + ":" + key;
  }

  private long getTimeoutSeconds() {
    if (randomTimeout) {
      return ThreadLocalRandom.current().nextLong(minTimeoutSeconds, maxTimeoutSeconds);
    }
    return timeoutSeconds;
  }
}
