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

import cn.idealframework.cache.MemoryCache;
import com.github.benmanes.caffeine.cache.Cache;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Function;

/**
 * @author 宋志宗 on 2021/7/9
 */
public class CaffeineCache<V> implements MemoryCache<V> {
  private final Cache<String, V> caffeine;

  protected CaffeineCache(Cache<String, V> caffeine) {
    this.caffeine = caffeine;
  }

  @Nullable
  @Override
  public V getIfPresent(@Nonnull String key) {
    return caffeine.getIfPresent(key);
  }

  @Nullable
  @Override
  public V get(@Nonnull String key, @Nonnull Function<String, ? extends V> function) {
    return caffeine.get(key, function);
  }

  @Override
  public void put(@Nonnull String key, @Nonnull V value) {
    caffeine.put(key, value);
  }

  @Override
  public void putAll(@Nonnull Map<String, ? extends V> map) {
    caffeine.putAll(map);
  }

  @Override
  public void invalidate(@Nonnull String key) {
    caffeine.invalidate(key);
  }

  @Override
  public void invalidateAll(@Nonnull Iterable<String> keys) {
    caffeine.invalidateAll(keys);
  }
}
