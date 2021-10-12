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
 * @author 宋志宗 on 2021/7/20
 */
public interface HashCache<V> {

  boolean hasKey(@Nonnull String key);

  boolean hasKey(@Nonnull String key, @Nonnull String hashKey);

  @Nonnull
  Map<String, V> getAllIfPresent(@Nonnull String key);

  @Nullable
  V getIfPresent(@Nonnull String key, @Nonnull String hashKey);

  @Nullable
  V get(@Nonnull String key, @Nonnull String hashKey,
        @Nonnull Function<String, ? extends V> function);

  void put(@Nonnull String key, @Nonnull String hashKey, @Nonnull V value);

  /** 批量写入 */
  void putAll(@Nonnull String key, @Nonnull Map<String, V> map);

  /** 清空原有的key, 然后写入缓存 */
  void setAll(@Nonnull String key, @Nonnull Map<String, V> map);

  /** 失效某一个keu */
  void invalidate(@Nonnull String key);

  /** 失效某一个hash key */
  void invalidate(@Nonnull String key, @Nonnull String hashKey);

  /** 批量失效key */
  void invalidateAll(@Nonnull Iterable<String> keys);
}
