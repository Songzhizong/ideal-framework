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

import cn.idealframework.cache.CacheUtils;
import cn.idealframework.cache.DistributedCache;
import cn.idealframework.cache.serialize.Deserializer;
import cn.idealframework.cache.serialize.Serializer;
import cn.idealframework.lang.Maps;
import cn.idealframework.lang.StringUtils;
import cn.idealframework.lock.DLock;
import cn.idealframework.lock.DLockFactory;
import cn.idealframework.util.Asserts;
import lombok.Setter;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Redis缓存构建器
 *
 * @author 宋志宗 on 2021/7/9
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@CommonsLog
public class RedisCache<V> implements DistributedCache<V> {
  @Setter
  private static String globalPrefix = "cache:";
  @Setter
  private static StringRedisTemplate redisTemplate;
  @Setter
  private static DLockFactory lockFactory;

  private final String prefix;
  private final boolean randomTimeout;
  private final long timeoutSeconds;
  private final long minTimeoutSeconds;
  private final long maxTimeoutSeconds;
  private final long nullCacheTimeoutSeconds;
  private final Serializer<V> serializer;
  private final Deserializer<V> deserializer;

  public RedisCache(@Nonnull String namespace,
                    boolean randomTimeout,
                    long timeoutSeconds,
                    long minTimeoutSeconds,
                    long maxTimeoutSeconds,
                    long nullCacheTimeoutSeconds,
                    @Nonnull Serializer<V> serializer,
                    @Nonnull Deserializer<V> deserializer) {
    Asserts.nonnull(RedisCache.redisTemplate,
      "RedisCache.redisTemplate为空, 请手动设置或配置ideal-boot-starter-redis");
    Asserts.nonnull(RedisCache.lockFactory,
      "RedisCache.lockFactory为空, 请手动设置或配置ideal-boot-starter-lock");
    this.prefix = globalPrefix + namespace;
    this.randomTimeout = randomTimeout;
    this.timeoutSeconds = Math.max(timeoutSeconds, 1L);
    this.minTimeoutSeconds = Math.max(minTimeoutSeconds, 1L);
    this.maxTimeoutSeconds = Math.max(maxTimeoutSeconds, 2L);
    this.nullCacheTimeoutSeconds = Math.max(nullCacheTimeoutSeconds, 1L);
    this.serializer = serializer;
    this.deserializer = deserializer;
  }


  @Nullable
  @Override
  public V getIfPresent(@Nonnull String key) {
    String redisKey = genRedisKey(key);
    return doGet(redisKey);
  }

  @Nullable
  @Override
  public V get(@Nonnull String key, @Nonnull Function<String, ? extends V> function) {
    Asserts.nonnull(function, "function must be not null");
    String redisKey = genRedisKey(key);
    V value = doGet(redisKey);
    if (value != null) {
      return value;
    }
    DLock lock = getLock(redisKey);
    V apply;
    try {
      lock.lock();
      value = doGet(redisKey);
      if (value != null) {
        return value;
      }
      apply = function.apply(key);
      if (apply != null) {
        String serialize = serializer.serialize(apply);
        redisTemplate.opsForValue().set(redisKey, serialize, getTimeoutSeconds(), TimeUnit.SECONDS);
      } else {
        redisTemplate.opsForValue().set(redisKey, CacheUtils.NULL_VALUE, nullCacheTimeoutSeconds, TimeUnit.SECONDS);
      }
    } finally {
      lock.unlock();
    }
    return apply;
  }

  @Nullable
  private V doGet(String redisKey) {
    String value = redisTemplate.opsForValue().get(redisKey);
    if (CacheUtils.isNullValue(value)) {
      return null;
    }
    return deserializer.deserialize(value);
  }

  @Override
  public void put(@Nonnull String key, @Nonnull V value) {
    Asserts.nonnull(value, "value must be not null");
    String redisKey = genRedisKey(key);
    String json = serializer.serialize(value);
    redisTemplate.opsForValue().set(redisKey, json, getTimeoutSeconds(), TimeUnit.SECONDS);
  }

  @Override
  public void putAll(@Nonnull Map<String, ? extends V> map) {
    if (Maps.isEmpty(map)) {
      return;
    }
    SessionCallback<Boolean> callback = new SessionCallback<Boolean>() {
      @Nonnull
      @Override
      public Boolean execute(@Nonnull RedisOperations operations) throws DataAccessException {
        ValueOperations opsForValue = operations.opsForValue();
        map.forEach((k, v) -> {
          if (v != null) {
            String redisKey = genRedisKey(k);
            String jsonString = serializer.serialize(v);
            opsForValue.set(redisKey, jsonString, getTimeoutSeconds(), TimeUnit.SECONDS);
          }
        });
        return true;
      }
    };
    redisTemplate.execute(callback);
  }

  @Override
  public void invalidate(@Nonnull String key) {
    String redisKey = genRedisKey(key);
    redisTemplate.delete(redisKey);
  }

  @Override
  public void invalidateAll(@Nonnull Iterable<String> keys) {
    List<String> redisKeys = new ArrayList<>();
    keys.forEach(key -> {
      if (StringUtils.isNotBlank(key)) {
        String redisKey = genRedisKey(key);
        redisKeys.add(redisKey);
      }
    });
    redisTemplate.delete(redisKeys);
  }

  @Nonnull
  private DLock getLock(@Nonnull String key) {
    return RedisCache.lockFactory.getLock(key);
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
