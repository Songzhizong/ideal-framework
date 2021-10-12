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
import java.util.*;

/**
 * @author 宋志宗 on 2021/7/6
 */
public final class ImmutableListBuilder<E> {
  private final List<E> list = new ArrayList<>();

  private ImmutableListBuilder() {
  }

  @Nonnull
  public static <E> ImmutableListBuilder<E> newBuilder() {
    return new ImmutableListBuilder<>();
  }

  @Nonnull
  public static <E> ImmutableListBuilder<E> newBuilder(E e) {
    return new ImmutableListBuilder<E>().add(e);
  }

  @Nonnull
  @SafeVarargs
  public static <E> ImmutableListBuilder<E> newBuilder(E... es) {
    return new ImmutableListBuilder<E>().addAll(es);
  }

  @Nonnull
  public static <E> ImmutableListBuilder<E> newBuilder(@Nonnull Collection<E> collection) {
    return new ImmutableListBuilder<E>().addAll(collection);
  }

  public ImmutableListBuilder<E> add(E e) {
    list.add(e);
    return this;
  }

  @SafeVarargs
  public final ImmutableListBuilder<E> addAll(E... es) {
    list.addAll(Arrays.asList(es));
    return this;
  }

  public ImmutableListBuilder<E> addAll(@Nonnull Collection<E> collection) {
    list.addAll(collection);
    return this;
  }

  @Nonnull
  public List<E> build() {
    return Collections.unmodifiableList(list);
  }
}
