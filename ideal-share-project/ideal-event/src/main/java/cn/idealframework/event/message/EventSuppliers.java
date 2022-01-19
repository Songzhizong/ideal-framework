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

import cn.idealframework.event.message.impl.EventSuppliersImpl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 宋志宗 on 2022/1/6
 */
public interface EventSuppliers {

  /**
   * 创建一个空的EventSuppliers
   *
   * @author 宋志宗 on 2022/1/19
   */
  @Nonnull
  static EventSuppliers empty() {
    return EventSuppliersImpl.empty();
  }

  @Nonnull
  static EventSuppliers of(@Nullable EventSupplier supplier) {
    return EventSuppliersImpl.of(supplier);
  }

  @Nonnull
  static EventSuppliers of(@Nullable List<EventSupplier> suppliers) {
    return EventSuppliersImpl.of(suppliers);
  }

  @Nonnull
  static EventSuppliers of(@Nullable EventSupplier... suppliers) {
    return EventSuppliersImpl.of(suppliers);
  }

  @Nonnull
  static EventSuppliers of(@Nullable EventSuppliers suppliers) {
    return EventSuppliersImpl.of(suppliers);
  }

  @Nonnull
  ArrayList<EventSupplier> get();

  boolean isEmpty();

  @Nonnull
  EventSuppliers add(@Nullable EventSupplier supplier);

  @Nonnull
  EventSuppliers add(@Nullable EventSuppliers suppliers);

  @Nonnull
  EventSuppliers add(@Nullable List<EventSupplier> suppliers);

  @Nonnull
  default EventSuppliers merge(@Nullable EventSuppliers suppliers) {
    return this.add(suppliers);
  }

  @Nonnull
  default EventSuppliers merge(@Nullable List<EventSupplier> otherSuppliers) {
    return this.add(otherSuppliers);
  }
}
