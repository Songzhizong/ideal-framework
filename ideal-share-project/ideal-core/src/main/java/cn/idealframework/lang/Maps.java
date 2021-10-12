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
package cn.idealframework.lang;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 宋志宗 on 2021/7/9
 */
public final class Maps {

  @Nonnull
  public static <K, V> Map<K, V> of() {
    return Collections.emptyMap();
  }

  @Nonnull
  public static <K, V> Map<K, V> of(@Nonnull K k, @Nonnull V v) {
    return Collections.singletonMap(k, v);
  }

  @Nonnull
  public static <K, V> Map<K, V> of(@Nonnull K k1, @Nonnull V v1,
                                    @Nonnull K k2, @Nonnull V v2) {
    Map<K, V> map = new HashMap<>(4);
    map.put(k1, v1);
    map.put(k2, v2);
    return Collections.unmodifiableMap(map);
  }

  @Nonnull
  public static <K, V> Map<K, V> of(@Nonnull K k1, @Nonnull V v1,
                                    @Nonnull K k2, @Nonnull V v2,
                                    @Nonnull K k3, @Nonnull V v3) {
    Map<K, V> map = new HashMap<>(8);
    map.put(k1, v1);
    map.put(k2, v2);
    map.put(k3, v3);
    return Collections.unmodifiableMap(map);
  }

  @Nonnull
  public static <K, V> Map<K, V> of(@Nonnull K k1, @Nonnull V v1,
                                    @Nonnull K k2, @Nonnull V v2,
                                    @Nonnull K k3, @Nonnull V v3,
                                    @Nonnull K k4, @Nonnull V v4) {
    Map<K, V> map = new HashMap<>(8);
    map.put(k1, v1);
    map.put(k2, v2);
    map.put(k3, v3);
    map.put(k4, v4);
    return Collections.unmodifiableMap(map);
  }

  @Nonnull
  public static <K, V> Map<K, V> of(@Nonnull K k1, @Nonnull V v1,
                                    @Nonnull K k2, @Nonnull V v2,
                                    @Nonnull K k3, @Nonnull V v3,
                                    @Nonnull K k4, @Nonnull V v4,
                                    @Nonnull K k5, @Nonnull V v5) {
    Map<K, V> map = new HashMap<>(8);
    map.put(k1, v1);
    map.put(k2, v2);
    map.put(k3, v3);
    map.put(k4, v4);
    map.put(k5, v5);
    return Collections.unmodifiableMap(map);
  }

  @Nonnull
  public static <K, V> Map<K, V> of(@Nonnull K k1, @Nonnull V v1,
                                    @Nonnull K k2, @Nonnull V v2,
                                    @Nonnull K k3, @Nonnull V v3,
                                    @Nonnull K k4, @Nonnull V v4,
                                    @Nonnull K k5, @Nonnull V v5,
                                    @Nonnull K k6, @Nonnull V v6) {
    Map<K, V> map = new HashMap<>(16);
    map.put(k1, v1);
    map.put(k2, v2);
    map.put(k3, v3);
    map.put(k4, v4);
    map.put(k5, v5);
    map.put(k6, v6);
    return Collections.unmodifiableMap(map);
  }

  @Nonnull
  public static <K, V> Map<K, V> of(@Nonnull Map<K, V> map) {
    return Collections.unmodifiableMap(map);
  }

  public static boolean isEmpty(@Nullable Map<?, ?> map) {
    return map == null || map.isEmpty();
  }

  public static boolean isNotEmpty(@Nullable Map<?, ?> map) {
    return !isEmpty(map);
  }

  private Maps() {
  }
}
