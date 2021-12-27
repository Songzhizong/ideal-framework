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
package cn.idealframework.event.message;

import cn.idealframework.event.tuple.EventTuple;
import cn.idealframework.lang.ArrayUtils;
import cn.idealframework.lang.Lists;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.annotation.Nonnull;
import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 宋志宗 on 2021/11/22
 */
@Setter(AccessLevel.PROTECTED)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class EventSuppliers {
  @Nonnull
  @Getter(AccessLevel.PROTECTED)
  private List<EventSupplier> suppliers;

  @Nonnull
  public static EventSuppliers empty() {
    return new EventSuppliers(new ArrayList<>());
  }

  @Nonnull
  public static EventSuppliers of(EventSupplier supplier) {
    if (supplier == null) {
      return empty();
    }
    return new EventSuppliers(Lists.arrayList(supplier));
  }

  @Nonnull
  public static EventSuppliers of(EventSupplier... suppliers) {
    if (ArrayUtils.isEmpty(suppliers)) {
      return empty();
    }
    return new EventSuppliers(Lists.arrayList(suppliers));
  }

  @Nonnull
  public static EventSuppliers of(@Nonnull EventTuple<?> eventTuple) {
    return empty().merge(eventTuple);
  }

  @Nonnull
  public List<EventSupplier> get() {
    return getSuppliers();
  }

  @Transient
  public boolean isEmpty() {
    return Lists.isEmpty(getSuppliers());
  }

  @Nonnull
  public EventSuppliers add(@Nonnull EventSupplier supplier) {
    this.getSuppliers().add(supplier);
    return this;
  }

  @Nonnull
  public EventSuppliers merge(@Nonnull EventSuppliers suppliers) {
    if (suppliers.isEmpty()) {
      return this;
    }
    if (this.isEmpty()) {
      return suppliers;
    }
    List<EventSupplier> otherSuppliers = suppliers.get();
    this.getSuppliers().addAll(otherSuppliers);
    return this;
  }

  @Nonnull
  public EventSuppliers merge(@Nonnull List<EventSupplier> otherSuppliers) {
    if (Lists.isEmpty(otherSuppliers)) {
      return this;
    }
    this.getSuppliers().addAll(otherSuppliers);
    return this;
  }

  @Nonnull
  public EventSuppliers merge(@Nonnull EventTuple<?> eventTuple) {
    List<EventSupplier> otherSuppliers = eventTuple.getSuppliers();
    return merge(otherSuppliers);
  }
}
