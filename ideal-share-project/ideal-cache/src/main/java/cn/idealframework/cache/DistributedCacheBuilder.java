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
package cn.idealframework.cache;

import cn.idealframework.cache.serialize.Deserializer;
import cn.idealframework.cache.serialize.Serializer;

import javax.annotation.Nonnull;
import java.time.Duration;

/**
 * 分布式缓存构建器接口
 *
 * @author 宋志宗 on 2021/7/10
 */
@SuppressWarnings("unused")
public interface DistributedCacheBuilder<V> {

  /**
   * 设置缓存过期时间, 自写入之后
   *
   * @param expireAfterWrite 过期时间
   * @author 宋志宗 on 2021/7/11
   */
  DistributedCacheBuilder<V> expireAfterWrite(@Nonnull Duration expireAfterWrite);

  /**
   * 设置缓存过期时间, 自写入之后
   *
   * @param minTimeout 最小过期时间
   * @param maxTimeout 最大过期时间
   * @author 宋志宗 on 2021/7/11
   */
  DistributedCacheBuilder<V> expireAfterWrite(@Nonnull Duration minTimeout,
                                              @Nonnull Duration maxTimeout);

  /**
   * 关闭空值缓存
   *
   * @author 宋志宗 on 2022/1/25
   */
  DistributedCacheBuilder<V> disableCacheNull();

  /**
   * 设置fallback的过期时间
   *
   * @param fallbackTimeout fallback的过期时间
   * @author 宋志宗 on 2021/7/11
   */
  DistributedCacheBuilder<V> nullCacheTimeout(@Nonnull Duration fallbackTimeout);

  /**
   * 设置序列化器
   *
   * @param serializer 序列化器
   * @author 宋志宗 on 2021/7/11
   */
  DistributedCacheBuilder<V> serializer(@Nonnull Serializer<V> serializer);

  /**
   * 设置反序列化器
   *
   * @param deserializer 反序列化器
   * @author 宋志宗 on 2021/7/11
   */
  DistributedCacheBuilder<V> deserializer(@Nonnull Deserializer<V> deserializer);

  /**
   * 构建DistributedCache
   *
   * @param namespace 命名空间, 为每种缓存类型设置不同的值
   * @return DistributedCache
   * @author 宋志宗 on 2021/7/11
   */
  DistributedCache<V> build(@Nonnull String namespace);
}
