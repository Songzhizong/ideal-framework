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
import cn.idealframework.cache.DistributedHashCacheBuilder;
import cn.idealframework.cache.serialize.Deserializer;
import cn.idealframework.cache.serialize.Serializer;
import cn.idealframework.util.Asserts;

import javax.annotation.Nonnull;
import java.time.Duration;

/**
 * @author 宋志宗 on 2021/7/20
 */
public class RedisHashCacheBuilder<V> implements DistributedHashCacheBuilder<V> {
  private boolean randomTimeout = false;
  // 缓存默认一天过期时间
  private long timeoutSeconds = 86400L;
  private long minTimeoutSeconds = -1L;
  private long maxTimeoutSeconds = -1L;
  private Serializer<V> serializer;
  private Deserializer<V> deserializer;

  @Override
  public DistributedHashCacheBuilder<V> expireAfterWrite(@Nonnull Duration expireAfterWrite) {
    Asserts.nonnull(expireAfterWrite, "expireAfterWrite must be not null");
    this.randomTimeout = false;
    this.timeoutSeconds = Math.max(expireAfterWrite.getSeconds(), 1);
    return this;
  }

  @Override
  public DistributedHashCacheBuilder<V> expireAfterWrite(@Nonnull Duration minTimeout,
                                                         @Nonnull Duration maxTimeout) {
    this.randomTimeout = true;
    this.minTimeoutSeconds = Math.max(minTimeout.getSeconds(), 1L);
    this.maxTimeoutSeconds = Math.max(maxTimeout.getSeconds(), 2L);
    return this;
  }

  @Override
  public DistributedHashCacheBuilder<V> serializer(@Nonnull Serializer<V> serializer) {
    Asserts.nonnull(serializer, "serializer must be not null");
    this.serializer = serializer;
    return this;
  }

  @Override
  public DistributedHashCacheBuilder<V> deserializer(@Nonnull Deserializer<V> deserializer) {
    Asserts.nonnull(deserializer, "deserializer must be not null");
    this.deserializer = deserializer;
    return this;
  }

  @Override
  public DistributedHashCache<V> build(@Nonnull String namespace) {
    Asserts.nonnull(serializer, "未设置缓存值序列化器");
    Asserts.nonnull(deserializer, "未设置缓存值反序列化器");
    return new RedisHashCache<>(namespace, randomTimeout, timeoutSeconds,
      minTimeoutSeconds, maxTimeoutSeconds, serializer, deserializer);
  }
}
