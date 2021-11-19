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
import java.util.*;

/**
 * @author 宋志宗 on 2021/7/8
 */
@SuppressWarnings({"DuplicatedCode", "unused"})
public final class Sets {

  public static boolean isEmpty(@Nullable Set<?> set) {
    return set == null || set.isEmpty();
  }

  public static boolean isNotEmpty(@Nullable Set<?> set) {
    return !Sets.isEmpty(set);
  }

  @Nonnull
  public static <E> Set<E> of() {
    return Collections.emptySet();
  }

  @Nonnull
  public static <E> Set<E> of(@Nonnull E e) {
    return Collections.singleton(e);
  }

  @Nonnull
  public static <E> Set<E> of(@Nonnull E e1, @Nonnull E e2) {
    Set<E> set = new HashSet<>(16);
    set.add(e1);
    set.add(e2);
    return Collections.unmodifiableSet(set);
  }

  @Nonnull
  public static <E> Set<E> of(@Nonnull E e1, @Nonnull E e2, @Nonnull E e3) {
    Set<E> set = new HashSet<>(16);
    set.add(e1);
    set.add(e2);
    set.add(e3);
    return Collections.unmodifiableSet(set);
  }

  @Nonnull
  public static <E> Set<E> of(@Nonnull E e1, @Nonnull E e2,
                              @Nonnull E e3, @Nonnull E e4) {
    Set<E> set = new HashSet<>(16);
    set.add(e1);
    set.add(e2);
    set.add(e3);
    set.add(e4);
    return Collections.unmodifiableSet(set);
  }

  @Nonnull
  public static <E> Set<E> of(@Nonnull E e1, @Nonnull E e2,
                              @Nonnull E e3, @Nonnull E e4, @Nonnull E e5) {
    Set<E> set = new HashSet<>(16);
    set.add(e1);
    set.add(e2);
    set.add(e3);
    set.add(e4);
    set.add(e5);
    return Collections.unmodifiableSet(set);
  }

  @Nonnull
  public static <E> Set<E> of(@Nonnull E e1, @Nonnull E e2, @Nonnull E e3,
                              @Nonnull E e4, @Nonnull E e5, @Nonnull E e6) {
    Set<E> set = new HashSet<>(16);
    set.add(e1);
    set.add(e2);
    set.add(e3);
    set.add(e4);
    set.add(e5);
    set.add(e6);
    return Collections.unmodifiableSet(set);
  }

  @Nonnull
  public static <E> Set<E> of(@Nonnull E e1, @Nonnull E e2,
                              @Nonnull E e3, @Nonnull E e4,
                              @Nonnull E e5, @Nonnull E e6, @Nonnull E e7) {
    Set<E> set = new HashSet<>(16);
    set.add(e1);
    set.add(e2);
    set.add(e3);
    set.add(e4);
    set.add(e5);
    set.add(e6);
    set.add(e7);
    return Collections.unmodifiableSet(set);
  }

  @Nonnull
  public static <E> Set<E> of(@Nonnull E e1, @Nonnull E e2,
                              @Nonnull E e3, @Nonnull E e4,
                              @Nonnull E e5, @Nonnull E e6,
                              @Nonnull E e7, @Nonnull E e8) {
    Set<E> set = new HashSet<>(16);
    set.add(e1);
    set.add(e2);
    set.add(e3);
    set.add(e4);
    set.add(e5);
    set.add(e6);
    set.add(e7);
    set.add(e8);
    return Collections.unmodifiableSet(set);
  }

  @Nonnull
  @SafeVarargs
  public static <E> Set<E> of(E... elements) {
    if (elements == null || elements.length == 0) {
      return of();
    }
    HashSet<E> hashSet = new HashSet<>(Arrays.asList(elements));
    return Collections.unmodifiableSet(hashSet);
  }

  @Nonnull
  public static <E> Set<E> merge(@Nonnull Collection<? extends E> c1,
                                 @Nonnull Collection<? extends E> c2) {
    Set<E> result = new HashSet<>();
    result.addAll(c1);
    result.addAll(c2);
    return result;
  }

  @Nonnull
  public static <E> Set<E> merge(@Nonnull Collection<? extends E> c1,
                                 @Nonnull Collection<? extends E> c2,
                                 @Nonnull Collection<? extends E> c3) {
    Set<E> result = new HashSet<>();
    result.addAll(c1);
    result.addAll(c2);
    result.addAll(c3);
    return result;
  }

  @Nonnull
  public static <E> Set<E> merge(@Nonnull Collection<? extends E> c1,
                                 @Nonnull Collection<? extends E> c2,
                                 @Nonnull Collection<? extends E> c3,
                                 @Nonnull Collection<? extends E> c4) {
    Set<E> result = new HashSet<>();
    result.addAll(c1);
    result.addAll(c2);
    result.addAll(c3);
    result.addAll(c4);
    return result;
  }

  @Nonnull
  public static <E> Set<E> merge(@Nonnull Collection<? extends E> c1,
                                 @Nonnull Collection<? extends E> c2,
                                 @Nonnull Collection<? extends E> c3,
                                 @Nonnull Collection<? extends E> c4,
                                 @Nonnull Collection<? extends E> c5) {
    Set<E> result = new HashSet<>();
    result.addAll(c1);
    result.addAll(c2);
    result.addAll(c3);
    result.addAll(c4);
    result.addAll(c5);
    return result;
  }

  @Nonnull
  public static <E> Set<E> merge(@Nonnull Collection<? extends E> c1,
                                 @Nonnull Collection<? extends E> c2,
                                 @Nonnull Collection<? extends E> c3,
                                 @Nonnull Collection<? extends E> c4,
                                 @Nonnull Collection<? extends E> c5,
                                 @Nonnull Collection<? extends E> c6) {
    Set<E> result = new HashSet<>();
    result.addAll(c1);
    result.addAll(c2);
    result.addAll(c3);
    result.addAll(c4);
    result.addAll(c5);
    result.addAll(c6);
    return result;
  }

  @Nonnull
  @SafeVarargs
  public static <E> Set<E> merge(@Nonnull Collection<? extends E>... cs) {
    Set<E> result = new HashSet<>();
    for (Collection<? extends E> c : cs) {
      result.addAll(c);
    }
    return result;
  }

  @Nonnull
  public static <E> List<List<E>> chunked(@Nonnull Set<E> set, int size) {
    return CollectionUtils.chunked(set, size);
  }
}
