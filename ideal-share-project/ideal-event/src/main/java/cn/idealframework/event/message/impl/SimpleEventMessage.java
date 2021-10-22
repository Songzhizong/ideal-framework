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

import cn.idealframework.event.message.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

/**
 * 事件消息默认实现
 *
 * @author 宋志宗 on 2021/4/22
 */
@Getter
@Setter
public class SimpleEventMessage<T> implements EventMessage<T> {
  @Nonnull
  private String uuid;

  @Nonnull
  private String topic;

  @Nullable
  private String aggregateType;

  @Nullable
  private String aggregateId;

  @Nullable
  @JsonDeserialize(using = EventHeadersDeserializer.class)
  private EventHeaders headers;

  @Nonnull
  private T payload;

  private long eventTime;

  public SimpleEventMessage() {
    this.uuid = generateUUID();
  }

  public SimpleEventMessage(@Nullable String uuid,
                            @Nonnull String topic,
                            @Nullable String aggregateType,
                            @Nullable String aggregateId,
                            @Nullable EventHeaders headers,
                            @Nonnull T payload,
                            long eventTime) {
    if (uuid == null) {
      uuid = generateUUID();
    }
    this.uuid = uuid;
    this.topic = topic;
    this.aggregateType = aggregateType;
    this.aggregateId = aggregateId;
    this.headers = headers;
    this.payload = payload;
    this.eventTime = eventTime;
  }

  @Nonnull
  public static <T extends Event> SimpleEventMessage<T> of(@Nonnull T event) {
    if (event instanceof DomainEvent) {
      DomainEvent domainEvent = (DomainEvent) event;
      return new SimpleEventMessage<>(
        null,
        domainEvent.getTopic(),
        domainEvent.getAggregateType(),
        domainEvent.getAggregateId(),
        EventHeaders.create(),
        event,
        System.currentTimeMillis()
      );
    } else {
      return new SimpleEventMessage<>(
        null,
        event.getTopic(),
        null,
        null,
        EventHeaders.create(),
        event,
        System.currentTimeMillis()
      );
    }
  }

  @Nonnull
  public static <T extends Event> SimpleEventMessage<T> of(@Nonnull T event,
                                                           @Nullable EventHeaders headers) {
    if (event instanceof DomainEvent) {
      DomainEvent domainEvent = (DomainEvent) event;
      return new SimpleEventMessage<>(
        null,
        domainEvent.getTopic(),
        domainEvent.getAggregateType(),
        domainEvent.getAggregateId(),
        headers,
        event,
        System.currentTimeMillis()
      );
    } else {
      return new SimpleEventMessage<>(
        null,
        event.getTopic(),
        null,
        null,
        headers,
        event,
        System.currentTimeMillis()
      );
    }
  }

  @Nonnull
  @Override
  public String uuid() {
    return uuid;
  }

  @Nonnull
  @Override
  public String getTopic() {
    return topic;
  }

  @Nullable
  @Override
  public String getAggregateType() {
    return aggregateType;
  }

  @Nullable
  @Override
  public String getAggregateId() {
    return aggregateId;
  }

  @Nullable
  @Override
  public EventHeaders getHeaders() {
    return headers;
  }

  @Nonnull
  @Override
  public T getPayload() {
    return payload;
  }

  @Override
  public long getEventTime() {
    return eventTime;
  }

  @Nonnull
  private String generateUUID() {
    String uuid = UUID.randomUUID().toString();
    char[] chars = new char[32];
    char[] charArray = uuid.toCharArray();
    int i = -1;
    for (char c : charArray) {
      if (c != '-') {
        chars[++i] = c;
      }
    }
    return String.valueOf(chars);
  }

}
