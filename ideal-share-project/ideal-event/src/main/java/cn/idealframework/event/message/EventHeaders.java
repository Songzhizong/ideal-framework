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

import cn.idealframework.event.message.impl.EventHeadersImpl;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Set;

/**
 * @author 宋志宗 on 2021/4/26
 */
public interface EventHeaders {

  @Nonnull
  static EventHeaders create() {
    return new EventHeadersImpl();
  }

  boolean isEmpty();

  Set<Object> get(@Nonnull String property);

  EventHeaders addString(@Nonnull String property, @Nonnull String value);

  EventHeaders addAllString(@Nonnull String property, @Nonnull Collection<String> values);

  EventHeaders addAllString(@Nonnull String property, @Nonnull String... values);

  EventHeaders addNumber(@Nonnull String property, long value);

  EventHeaders addAllNumber(@Nonnull String property, @Nonnull Collection<Long> values);

  EventHeaders addAllNumber(@Nonnull String property, @Nonnull long... values);

  boolean containsNumber(@Nonnull String property, long value);

  boolean containsString(@Nonnull String property, @Nonnull String value);
}
