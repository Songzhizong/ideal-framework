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
import java.util.stream.Collectors;

/**
 * 事件头
 *
 * @author 宋志宗 on 2020/5/26
 */
public class EventHeadersImpl implements EventHeaders, Map<String, Set<Object>>, Serializable {
  private static final long serialVersionUID = 117500675943825408L;
  private final Map<String, Set<Object>> targetMap;

  @Nonnull
  public static EventHeadersImpl create() {
    return new EventHeadersImpl();
  }

  public EventHeadersImpl() {
    this.targetMap = new LinkedHashMap<>();
  }

  @Override
  @Nonnull
  public EventHeaders addString(@Nonnull String key, @Nonnull String value) {
    Set<Object> values = this.targetMap.computeIfAbsent(key, k -> new LinkedHashSet<>());
    values.add(value);
    return this;
  }

  @Override
  @Nonnull
  public EventHeaders addAllString(@Nonnull String key, @Nonnull Collection<String> values) {
    Set<Object> currentValues = this.targetMap.computeIfAbsent(key, k -> new LinkedHashSet<>());
    currentValues.addAll(values);
    return this;
  }

  @Override
  public EventHeaders addAllString(@Nonnull String property, @Nonnull String... values) {
    return this.addAllString(property, Arrays.asList(values));
  }

  @Override
  @Nonnull
  public EventHeaders addNumber(@Nonnull String key, long value) {
    Set<Object> values = this.targetMap.computeIfAbsent(key, k -> new LinkedHashSet<>());
    values.add(value);
    return this;
  }

  @Override
  @Nonnull
  public EventHeaders addAllNumber(@Nonnull String key, @Nonnull Collection<Long> values) {
    Set<Object> currentValues = this.targetMap.computeIfAbsent(key, k -> new LinkedHashSet<>());
    List<Long> collect = values.stream().map(Number::longValue).collect(Collectors.toList());
    currentValues.addAll(collect);
    return this;
  }

  @Override
  public EventHeaders addAllNumber(@Nonnull String property, @Nonnull long... values) {
    List<Long> list = new ArrayList<>(values.length);
    for (long value : values) {
      list.add(value);
    }
    return this.addAllNumber(property, list);
  }

  @Override
  public boolean containsNumber(@Nonnull String property, long value) {
    Set<Object> objects = this.get(property);
    if (objects == null || objects.isEmpty()) {
      return false;
    }
    for (Object object : objects) {
      if (object instanceof Long) {
        if (value == (long) object) {
          return true;
        }
      } else if (object instanceof Number) {
        if (value == ((Number) object).longValue()) {
          return true;
        }
      } else if (object instanceof CharSequence) {
        CharSequence charSequence = (CharSequence) object;
        String string = charSequence.toString();
        String stringValue = String.valueOf(value);
        if (stringValue.equals(string)) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public boolean containsString(@Nonnull String property, @Nonnull String value) {
    Set<Object> objects = this.get(property);
    if (objects == null || objects.isEmpty()) {
      return false;
    }
    if (objects.contains(value)) {
      return true;
    }
    for (Object object : objects) {
      if (object instanceof String) {
        if (value.equals(object)) {
          return true;
        }
      } else if (object instanceof CharSequence) {
        CharSequence charSequence = (CharSequence) object;
        if (value.equals(charSequence.toString())) {
          return true;
        }
      } else {
        if (value.equals(object.toString())) {
          return true;
        }
      }
    }
    return false;
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
  public Set<Object> get(@Nonnull String property) {
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
  public Set<Object> get(Object key) {
    return this.targetMap.get(key);
  }

  @Nullable
  @Override
  public Set<Object> put(String key, Set<Object> value) {
    return this.targetMap.put(key, value);
  }

  @Override
  public Set<Object> remove(Object key) {
    return this.targetMap.remove(key);
  }

  @Override
  public void putAll(@Nonnull Map<? extends String, ? extends Set<Object>> m) {
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
  public Collection<Set<Object>> values() {
    return this.targetMap.values();
  }

  @Nonnull
  @Override
  public Set<Entry<String, Set<Object>>> entrySet() {
    return this.targetMap.entrySet();
  }
}
