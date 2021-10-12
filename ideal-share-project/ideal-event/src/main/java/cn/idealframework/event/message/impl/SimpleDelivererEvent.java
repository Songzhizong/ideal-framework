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

import cn.idealframework.event.message.DeliverEventMessage;
import cn.idealframework.event.message.EventMessage;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2021/4/23
 */
@Getter
@Setter
public class SimpleDelivererEvent extends SimpleEventMessage<Object> implements DeliverEventMessage {
  @Nullable
  private String listenerName;

  @Nonnull
  public static SimpleDelivererEvent of(@Nonnull EventMessage<Object> eventMessage) {
    SimpleDelivererEvent event = new SimpleDelivererEvent();
    event.setTopic(eventMessage.getTopic());
    event.setPayload(eventMessage.getPayload());
    event.setAggregateType(eventMessage.getAggregateType());
    event.setAggregateId(eventMessage.getAggregateId());
    event.setEventTime(eventMessage.getEventTime());
    event.setListenerName(null);
    return event;
  }

  @Nonnull
  public static SimpleDelivererEvent of(@Nonnull EventMessage<Object> eventMessage,
                                        @Nullable String listenerName) {
    SimpleDelivererEvent event = SimpleDelivererEvent.of(eventMessage);
    event.setListenerName(listenerName);
    return event;
  }

  @Nullable
  @Override
  public String getListenerName() {
    return listenerName;
  }
}
