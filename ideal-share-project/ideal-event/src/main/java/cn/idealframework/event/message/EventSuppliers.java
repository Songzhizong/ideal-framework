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
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author 宋志宗 on 2021/11/22
 */
@Setter(AccessLevel.PROTECTED)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class EventSuppliers {
  @Nonnull
  private List<EventSupplier> suppliers;

  @Nonnull
  public static EventSuppliers empty() {
    return new EventSuppliers(Lists.of());
  }

  @Nonnull
  public static EventSuppliers of(EventSupplier supplier) {
    if (supplier == null) {
      return new EventSuppliers(Lists.of());
    }
    return new EventSuppliers(Lists.of(supplier));
  }

  @Nonnull
  public static EventSuppliers of(EventSupplier... suppliers) {
    if (ArrayUtils.isEmpty(suppliers)) {
      return new EventSuppliers(Lists.of());
    }
    return new EventSuppliers(Arrays.asList(suppliers));
  }

  @Nonnull
  public static EventSuppliers of(@Nonnull EventTuple<?> eventTuple) {
    List<EventSupplier> suppliers = eventTuple.getSuppliers();
    return new EventSuppliers(suppliers);
  }

  @Nonnull
  public List<EventSupplier> get() {
    return suppliers;
  }

  @Nonnull
  public EventSuppliers add(@Nonnull EventSupplier supplier) {
    List<EventSupplier> currentSuppliers = this.get();
    List<EventSupplier> list = new ArrayList<>(currentSuppliers.size() + 1);
    list.addAll(currentSuppliers);
    list.add(supplier);
    this.setSuppliers(Lists.unmodifiable(list));
    return this;
  }

  @Nonnull
  public EventSuppliers merge(@Nonnull EventSuppliers suppliers) {
    List<EventSupplier> currentSuppliers = this.get();
    List<EventSupplier> otherSuppliers = suppliers.get();
    List<EventSupplier> merge = Lists.merge(currentSuppliers, otherSuppliers);
    this.setSuppliers(Lists.unmodifiable(merge));
    return this;
  }

  @Nonnull
  public EventSuppliers merge(@Nonnull List<EventSupplier> otherSuppliers) {
    List<EventSupplier> currentSuppliers = this.get();
    List<EventSupplier> merge = Lists.merge(currentSuppliers, otherSuppliers);
    this.setSuppliers(Lists.unmodifiable(merge));
    return this;
  }

  @Nonnull
  public EventSuppliers merge(@Nonnull EventTuple<?> eventTuple) {
    List<EventSupplier> currentSuppliers = this.get();
    List<EventSupplier> otherSuppliers = eventTuple.getSuppliers();
    List<EventSupplier> merge = Lists.merge(currentSuppliers, otherSuppliers);
    this.setSuppliers(Lists.unmodifiable(merge));
    return this;
  }
}
