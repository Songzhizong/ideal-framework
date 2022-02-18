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
import cn.idealframework.event.message.EventContext;
import cn.idealframework.event.message.EventHeaders;
import cn.idealframework.event.message.EventMessage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2021/4/23
 */
public class SimpleEventContext<T> implements EventContext<T> {
  private final EventMessage<T> eventMessage;

  public SimpleEventContext(EventMessage<T> eventMessage) {
    this.eventMessage = eventMessage;
  }

  @Nonnull
  public static SimpleEventContext<Object> of(@Nonnull DeliverEventMessage message, Object param) {
    SimpleEventMessage<Object> eventMessage = new SimpleEventMessage<>();
    eventMessage.setUuid(message.uuid());
    eventMessage.setTopic(message.getTopic());
    eventMessage.setAggregateType(message.getAggregateType());
    eventMessage.setAggregateId(message.getAggregateId());
    eventMessage.setHeaders(message.getHeaders());
    eventMessage.setPayload(param);
    eventMessage.setEventTime(message.getEventTime());
    return new SimpleEventContext<>(eventMessage);
  }

  @Nonnull
  public static SimpleEventContext<String> ofStringPayload(@Nonnull DeliverEventMessage message, String param) {
    SimpleEventMessage<String> eventMessage = new SimpleEventMessage<>();
    eventMessage.setUuid(message.uuid());
    eventMessage.setTopic(message.getTopic());
    eventMessage.setAggregateType(message.getAggregateType());
    eventMessage.setAggregateId(message.getAggregateId());
    eventMessage.setHeaders(message.getHeaders());
    eventMessage.setPayload(param);
    eventMessage.setEventTime(message.getEventTime());
    return new SimpleEventContext<>(eventMessage);
  }

  @Nonnull
  @Override
  public String uuid() {
    return eventMessage.uuid();
  }

  @Nullable
  @Override
  public String getAggregateType() {
    return eventMessage.getAggregateType();
  }

  @Nullable
  @Override
  public String getAggregateId() {
    return eventMessage.getAggregateId();
  }

  @Nullable
  @Override
  public EventHeaders getHeaders() {
    return eventMessage.getHeaders();
  }

  @Nonnull
  @Override
  public String getTopic() {
    return eventMessage.getTopic();
  }

  @Override
  public long getEventTime() {
    return eventMessage.getEventTime();
  }

  @Nonnull
  @Override
  public T getPayload() {
    return eventMessage.getPayload();
  }
}
