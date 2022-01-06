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
package cn.idealframework.event.message.impl;

import cn.idealframework.event.message.EventSupplier;
import cn.idealframework.event.message.EventSuppliers;
import cn.idealframework.lang.ArrayUtils;
import cn.idealframework.lang.Lists;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 宋志宗 on 2021/11/22
 */
@Setter(AccessLevel.PROTECTED)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class EventSuppliersImpl implements EventSuppliers {
  @Nonnull
  @Getter(AccessLevel.PROTECTED)
  private ArrayList<EventSupplier> suppliers;

  @Nonnull
  public static EventSuppliers empty() {
    return new EventSuppliersImpl(new ArrayList<>());
  }

  @Nonnull
  public static EventSuppliers of(@Nullable EventSupplier supplier) {
    if (supplier == null) {
      return empty();
    }
    return new EventSuppliersImpl(Lists.arrayList(supplier));
  }

  @Nonnull
  public static EventSuppliers of(@Nullable List<EventSupplier> suppliers) {
    if (Lists.isEmpty(suppliers)) {
      return empty();
    }
    return new EventSuppliersImpl(new ArrayList<>(suppliers));
  }

  @Nonnull
  public static EventSuppliers of(@Nullable EventSupplier... suppliers) {
    if (ArrayUtils.isEmpty(suppliers)) {
      return empty();
    }
    return new EventSuppliersImpl(Lists.arrayList(suppliers));
  }

  @Nonnull
  public static EventSuppliers of(@Nullable EventSuppliers suppliers) {
    if (suppliers == null || suppliers.isEmpty()) {
      return empty();
    }
    return new EventSuppliersImpl(suppliers.get());
  }

  @Nonnull
  @Override
  public ArrayList<EventSupplier> get() {
    return getSuppliers();
  }

  @Transient
  @Override
  public boolean isEmpty() {
    return Lists.isEmpty(getSuppliers());
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
    ArrayList<EventSupplier> otherSuppliers = suppliers.get();
    if (this.isEmpty()) {
      this.setSuppliers(otherSuppliers);
      return this;
    }
    this.getSuppliers().addAll(otherSuppliers);
    return this;
  }

  @Nonnull
  @Override
  public EventSuppliers add(@Nullable List<EventSupplier> suppliers) {
    if (Lists.isEmpty(suppliers)) {
      return this;
    }
    this.getSuppliers().addAll(suppliers);
    return this;
  }
}
