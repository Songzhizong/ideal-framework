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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author 宋志宗 on 2021/4/27
 */
@Getter
public class EventTuple<R> implements EventSuppliers {
  /** 方法返回结果 */
  @Nonnull
  private final R result;

  /** 方法返回事件列表 */
  @Nonnull
  @Setter(AccessLevel.PRIVATE)
  private ArrayList<EventSupplier> suppliers;

  protected EventTuple(@Nonnull R result,
                       @Nonnull ArrayList<EventSupplier> suppliers) {
    this.result = result;
    this.suppliers = suppliers;
  }

  @Nonnull
  public static <R> EventTuple<R> of(@Nonnull R result) {
    return new EventTuple<>(result, new ArrayList<>());
  }

  @Nonnull
  public static <R> EventTuple<R> of(@Nonnull R result,
                                     @Nonnull List<EventSupplier> suppliers) {
    return new EventTuple<>(result, new ArrayList<>(suppliers));
  }

  @Nonnull
  public static <R> EventTuple<R> of(@Nonnull R result,
                                     @Nonnull EventSupplier supplier) {
    return new EventTuple<>(result, Lists.arrayList(supplier));
  }

  @Nonnull
  public static <R> EventTuple<R> of(@Nonnull R result,
                                     @Nonnull EventSupplier... suppliers) {
    return new EventTuple<>(result, new ArrayList<>(Arrays.asList(suppliers)));
  }

  @Nonnull
  @Override
  public ArrayList<EventSupplier> get() {
    return this.getSuppliers();
  }

  @Override
  public boolean isEmpty() {
    return Lists.isEmpty(this.getSuppliers());
  }

  @Nonnull
  @Override
  public EventSuppliers add(@Nullable EventSupplier supplier) {
    if (supplier == null) {
      return this;
    }
    this.getSuppliers().add(supplier);
    return this;
  }

  @Nonnull
  @Override
  public EventSuppliers add(@Nullable EventSuppliers suppliers) {
    if (suppliers == null || suppliers.isEmpty()) {
      return this;
    }
    if (this.isEmpty()) {
      this.setSuppliers(suppliers.get());
      return this;
    }
    this.getSuppliers().addAll(suppliers.get());
    return this;
  }

  @Nonnull
  public EventTuple<R> add(@Nullable List<EventSupplier> suppliers) {
    if (Lists.isEmpty(suppliers)) {
      return this;
    }
    if (this.isEmpty()) {
      if (suppliers instanceof ArrayList) {
        this.setSuppliers((ArrayList<EventSupplier>) suppliers);
      } else {
        this.setSuppliers(new ArrayList<>(suppliers));
      }
      return this;
    }
    this.getSuppliers().addAll(suppliers);
    return this;
  }

  @Nonnull
  public EventSuppliers toEventSuppliers() {
    return this;
  }
}
