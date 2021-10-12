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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Function;

/**
 * 缓存接口
 *
 * @author 宋志宗 on 2021/7/9
 */
public interface Cache<V> {

  /**
   * 从缓存中获取指定键对应的值, 未命中缓存则立即返回null
   *
   * @param key 缓存键
   * @return 缓存值, 未命中缓存则返回null
   * @author 宋志宗 on 2021/7/11
   */
  @Nullable
  V getIfPresent(@Nonnull String key);

  /**
   * 从缓存中获取指定键对应的值, 不存在则调用 function 获取值
   *
   * @param key      缓存键
   * @param function 未命中缓存时调用此函数获取值并写入缓存
   * @return 值
   * @author 宋志宗 on 2021/7/11
   */
  @Nullable
  V get(@Nonnull String key, @Nonnull Function<String, ? extends V> function);

  /**
   * 写入缓存
   *
   * @param key   缓存键
   * @param value 缓存值
   * @author 宋志宗 on 2021/7/11
   */
  void put(@Nonnull String key, @Nonnull V value);

  /**
   * 批量写入缓存
   *
   * @param map 需要缓存的键值对
   * @author 宋志宗 on 2021/7/11
   */
  void putAll(@Nonnull Map<String, ? extends V> map);

  /**
   * 失效指定键
   *
   * @param key 缓存键
   * @author 宋志宗 on 2021/7/11
   */
  void invalidate(@Nonnull String key);

  /**
   * 批量失效缓存键
   *
   * @param keys 缓存键列表
   * @author 宋志宗 on 2021/7/11
   */
  void invalidateAll(@Nonnull Iterable<String> keys);
}
