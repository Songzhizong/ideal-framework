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

import cn.idealframework.event.message.EventHeaders;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.*;

/**
 * 事件头
 *
 * @author 宋志宗 on 2020/5/26
 */
public class EventHeadersImpl implements EventHeaders, Map<String, Set<String>>, Serializable {
  private static final long serialVersionUID = 117500675943825408L;
  private final Map<String, Set<String>> targetMap = new LinkedHashMap<>();

  @Nonnull
  public static EventHeadersImpl create() {
    return new EventHeadersImpl();
  }

  public EventHeadersImpl() {
  }

  @Nonnull
  @Override
  public EventHeaders add(@Nonnull String property, @Nonnull String value) {
    Set<String> strings = targetMap.computeIfAbsent(property, k -> new LinkedHashSet<>());
    strings.add(value);
    return this;
  }

  @Nonnull
  @Override
  public EventHeaders addAll(@Nonnull String property, @Nonnull Collection<String> values) {
    Set<String> strings = targetMap.computeIfAbsent(property, k -> new LinkedHashSet<>());
    strings.addAll(values);
    return this;
  }

  @Nonnull
  @Override
  public EventHeaders addAll(@Nonnull String property, @Nullable String... values) {
    if (values == null || values.length == 0) {
      return this;
    }
    Set<String> strings = targetMap.computeIfAbsent(property, k -> new LinkedHashSet<>());
    strings.addAll(Arrays.asList(values));
    return this;
  }

  @Nonnull
  @Override
  public EventHeaders set(@Nonnull String property, @Nonnull String value) {
    LinkedHashSet<String> strings = new LinkedHashSet<>();
    strings.add(value);
    targetMap.put(property, strings);
    return this;
  }

  @Nonnull
  @Override
  public EventHeaders setAll(@Nonnull String property, @Nonnull Collection<String> values) {
    LinkedHashSet<String> strings = new LinkedHashSet<>(values);
    targetMap.put(property, strings);
    return this;
  }

  @Nonnull
  @Override
  public EventHeaders setAll(@Nonnull String property, @Nullable String... values) {
    LinkedHashSet<String> strings;
    if (values == null || values.length == 0) {
      strings = new LinkedHashSet<>();
    } else {
      strings = new LinkedHashSet<>(Arrays.asList(values));
    }
    targetMap.put(property, strings);
    return this;
  }

  // Map implementation

  @Override
  public int size() {
    return this.targetMap.size();
  }

  @Override
  public boolean isEmpty() {
    return this.targetMap.isEmpty();
  }

  @Override
  public Set<String> get(@Nonnull String property) {
    return this.targetMap.get(property);
  }

  @Override
  public boolean containsKey(Object key) {
    return this.targetMap.containsKey(key);
  }

  @Override
  public boolean containsValue(Object value) {
    return this.targetMap.containsValue(value);
  }

  @Override
  public Set<String> get(Object key) {
    return this.targetMap.get(key);
  }

  @Nullable
  @Override
  public Set<String> put(String key, Set<String> value) {
    return this.targetMap.put(key, value);
  }

  @Override
  public Set<String> remove(Object key) {
    return this.targetMap.remove(key);
  }

  @Override
  public void putAll(@Nonnull Map<? extends String, ? extends Set<String>> m) {
    this.targetMap.putAll(m);
  }

  @Override
  public void clear() {
    this.targetMap.clear();
  }

  @Nonnull
  @Override
  public Set<String> keySet() {
    return this.targetMap.keySet();
  }

  @Nonnull
  @Override
  public Collection<Set<String>> values() {
    return this.targetMap.values();
  }

  @Nonnull
  @Override
  public Set<Entry<String, Set<String>>> entrySet() {
    return this.targetMap.entrySet();
  }
}
