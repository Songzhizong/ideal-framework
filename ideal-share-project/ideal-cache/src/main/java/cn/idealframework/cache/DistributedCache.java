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

import cn.idealframework.cache.impl.SmartDistributedCacheBuilder;

import javax.annotation.Nonnull;
import java.util.function.Function;

/**
 * @author 宋志宗 on 2021/7/10
 */
public interface DistributedCache<V> extends Cache<V> {


  @Nonnull
  static <V> DistributedCacheBuilder<V> newBuilder() {
    return SmartDistributedCacheBuilder.newBuilder();
  }

  /**
   * 从缓存中获取指定键对应的值, 不存在则调用 function 获取值, 若 function 返回null则使用fallback
   *
   * @param key      缓存键
   * @param function 未命中缓存时调用此函数获取值并写入缓存
   * @param fallback function返回null是将此值写入缓存并返还
   * @return 值
   * @author 宋志宗 on 2021/7/11
   */
  @Nonnull
  V get(@Nonnull String key, @Nonnull Function<String, ? extends V> function, @Nonnull V fallback);
}
