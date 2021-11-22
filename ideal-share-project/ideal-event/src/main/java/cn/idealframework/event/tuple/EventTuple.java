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
import cn.idealframework.event.message.EventSuppliers;
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
public class EventTuple<R> {
  /** 方法返回结果 */
  @Nonnull
  private final R result;

  /** 方法返回事件列表 */
  @Nonnull
  private List<EventSupplier> suppliers;

  protected EventTuple(@Nonnull R result,
                       @Nonnull List<EventSupplier> suppliers) {
    this.result = result;
    this.suppliers = suppliers;
  }

  @Nonnull
  public static <R> EventTuple<R> of(@Nonnull R result) {
    return new EventTuple<>(result, Lists.of());
  }

  @Nonnull
  public static <R> EventTuple<R> of(@Nonnull R result,
                                     @Nonnull List<EventSupplier> suppliers) {
    return new EventTuple<>(result, suppliers);
  }

  @Nonnull
  public static <R> EventTuple<R> of(@Nonnull R result,
                                     @Nonnull EventSupplier supplier) {
    return new EventTuple<>(result, Lists.of(supplier));
  }

  @Nonnull
  public static <R> EventTuple<R> of(@Nonnull R result,
                                     @Nonnull EventSupplier... suppliers) {
    return new EventTuple<>(result, Arrays.asList(suppliers));
  }

  @Nonnull
  public EventTuple<R> add(@Nullable List<EventSupplier> suppliers) {
    if (Lists.isEmpty(suppliers)) {
      return this;
    }
    List<EventSupplier> eventBuilders = this.getSuppliers();
    List<EventSupplier> merge = Lists.merge(eventBuilders, suppliers);
    this.setSuppliers(merge);
    return this;
  }

  @Nonnull
  public EventSuppliers toEventSuppliers() {
    return EventSuppliers.of(this);
  }

  private void setSuppliers(@Nonnull List<EventSupplier> suppliers) {
    this.suppliers = suppliers;
  }
}
