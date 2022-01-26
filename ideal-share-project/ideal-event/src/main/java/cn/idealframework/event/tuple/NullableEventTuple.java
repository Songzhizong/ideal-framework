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
package cn.idealframework.event.tuple;

import cn.idealframework.event.message.EventSupplier;
import cn.idealframework.lang.Lists;
import lombok.Getter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

/**
 * @author 宋志宗 on 2021/4/27
 */
@Getter
@Deprecated
public class NullableEventTuple<V> {
  /** 方法返回结果 */
  @Nullable
  private final V value;

  /** 方法返回事件列表 */
  @Nonnull
  private final List<EventSupplier> suppliers;

  protected NullableEventTuple(@Nullable V value,
                               @Nonnull List<EventSupplier> suppliers) {
    this.value = value;
    this.suppliers = suppliers;
  }

  @Nonnull
  public static <V> NullableEventTuple<V> of(@Nullable V value,
                                             @Nonnull List<EventSupplier> suppliers) {
    return new NullableEventTuple<>(value, suppliers);
  }

  @Nonnull
  public static <V> NullableEventTuple<V> of(@Nullable V value,
                                             @Nonnull EventSupplier supplier) {
    return new NullableEventTuple<>(value, Lists.of(supplier));
  }

  @Nonnull
  public static <V> NullableEventTuple<V> of(@Nullable V value,
                                             @Nonnull EventSupplier... suppliers) {
    return new NullableEventTuple<>(value, Arrays.asList(suppliers));
  }
}
