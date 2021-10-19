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

import cn.idealframework.event.message.impl.SimpleEventMessage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.beans.Transient;

/**
 * 事件消息
 *
 * @author 宋志宗 on 2021/4/22
 */
public interface EventMessage<T> extends EventSupplier {
  @Nonnull
  static <T extends DomainEvent> EventMessage<T> of(@Nonnull T event) {
    return SimpleEventMessage.of(event);
  }

  @Nonnull
  static <T extends DomainEvent> EventMessage<T> of(@Nonnull T event,
                                                    @Nullable EventHeaders headers) {
    return SimpleEventMessage.of(event, headers);
  }

  /**
   * @return 实体名称
   * @author 宋志宗 on 2021/4/22
   */
  @Nullable
  String getAggregateType();

  /**
   * @return 实体唯一id
   * @author 宋志宗 on 2021/4/22
   */
  @Nullable
  String getAggregateId();

  @Nullable
  EventHeaders getHeaders();

  @Nonnull
  @Transient
  String uuid();

  /**
   * @return 事件产生de时间戳
   * @author 宋志宗 on 2021/4/26
   */
  long getEventTime();

  /**
   * @return 事件主题
   * @author 宋志宗 on 2021/4/22
   */
  @Nonnull
  String getTopic();

  /**
   * @return 事件内容
   * @author 宋志宗 on 2021/4/22
   */
  @Nonnull
  T getPayload();

  @Nonnull
  @Override
  default EventMessage<?> get() {
    return this;
  }
}
