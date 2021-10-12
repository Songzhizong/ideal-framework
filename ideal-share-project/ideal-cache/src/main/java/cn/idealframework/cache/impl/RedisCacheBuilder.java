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

import cn.idealframework.cache.DistributedCache;
import cn.idealframework.cache.DistributedCacheBuilder;
import cn.idealframework.cache.serialize.Deserializer;
import cn.idealframework.cache.serialize.Serializer;
import cn.idealframework.util.Asserts;

import javax.annotation.Nonnull;
import java.time.Duration;

/**
 * @author 宋志宗 on 2021/7/9
 */
public class RedisCacheBuilder<V> implements DistributedCacheBuilder<V> {
  private V defaultFallback;
  private boolean randomTimeout = false;
  // 缓存默认一天过期时间
  private long timeoutSeconds = 86400L;
  private long minTimeoutSeconds = -1L;
  private long maxTimeoutSeconds = -1L;
  // fallback默认一分钟过期时间
  private long fallbackTimeoutSeconds = 60;
  private Serializer<V> serializer;
  private Deserializer<V> deserializer;

  @Override
  public DistributedCacheBuilder<V> defaultFallback(@Nonnull V defaultFallback) {
    this.defaultFallback = defaultFallback;
    return this;
  }

  @Override
  public RedisCacheBuilder<V> expireAfterWrite(@Nonnull Duration expireAfterWrite) {
    Asserts.nonnull(expireAfterWrite, "expireAfterWrite must be not null");
    this.randomTimeout = false;
    this.timeoutSeconds = Math.max(expireAfterWrite.getSeconds(), 1);
    return this;
  }

  @Override
  public DistributedCacheBuilder<V> expireAfterWrite(@Nonnull Duration minTimeout,
                                                     @Nonnull Duration maxTimeout) {
    this.randomTimeout = true;
    this.minTimeoutSeconds = Math.max(minTimeout.getSeconds(), 1L);
    this.maxTimeoutSeconds = Math.max(maxTimeout.getSeconds(), 2L);
    return this;
  }

  @Override
  public RedisCacheBuilder<V> fallbackTimeout(@Nonnull Duration fallbackTimeout) {
    Asserts.nonnull(fallbackTimeout, "fallbackTimeout must be not null");
    this.fallbackTimeoutSeconds = fallbackTimeout.getSeconds();
    return this;
  }

  @Override
  public RedisCacheBuilder<V> serializer(@Nonnull Serializer<V> serializer) {
    Asserts.nonnull(serializer, "serializer must be not null");
    this.serializer = serializer;
    return this;
  }

  @Override
  public RedisCacheBuilder<V> deserializer(@Nonnull Deserializer<V> deserializer) {
    Asserts.nonnull(deserializer, "deserializer must be not null");
    this.deserializer = deserializer;
    return this;
  }

  @Override
  public DistributedCache<V> build(@Nonnull String namespace) {
    Asserts.nonnull(serializer, "未设置缓存值序列化器");
    Asserts.nonnull(deserializer, "未设置缓存值反序列化器");
    return new RedisCache<>(namespace, defaultFallback, randomTimeout, timeoutSeconds,
      minTimeoutSeconds, maxTimeoutSeconds, fallbackTimeoutSeconds, serializer, deserializer);
  }
}
