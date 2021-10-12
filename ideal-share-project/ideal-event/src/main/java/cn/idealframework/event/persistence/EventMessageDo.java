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
package cn.idealframework.event.persistence;

import cn.idealframework.event.message.DomainEvent;
import cn.idealframework.event.message.EventHeaders;
import cn.idealframework.event.message.EventMessage;
import cn.idealframework.json.JsonUtils;
import cn.idealframework.lang.StringUtils;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2021/4/27
 */
@Getter
@Setter
public class EventMessageDo {
  @Nonnull
  private String uuid;

  @Nonnull
  private String topic;

  @Nonnull
  private String aggregateType;

  @Nonnull
  private String aggregateId;

  @Nonnull
  private String headers;

  @Nonnull
  private String payload;

  private long eventTime;

  @Nonnull
  public EventMessageDo create(@Nonnull EventMessage<? extends DomainEvent> message) {
    EventMessageDo eventMessageDo = new EventMessageDo();
    eventMessageDo.setUuid(message.uuid());
    eventMessageDo.setTopic(message.getTopic());
    eventMessageDo.setAggregateType(message.getAggregateType());
    eventMessageDo.setAggregateId(message.getAggregateId());
    EventHeaders headers = message.getHeaders();
    if (headers == null) {
      eventMessageDo.setHeaders(null);
    } else {
      eventMessageDo.setHeaders(JsonUtils.toJsonString(headers));
    }
    eventMessageDo.setPayload(JsonUtils.toJsonString(message.getPayload()));
    eventMessageDo.setEventTime(message.getEventTime());
    return eventMessageDo;
  }

  public void setAggregateType(@Nullable String aggregateType) {
    if (StringUtils.isBlank(aggregateType)) {
      this.aggregateType = "";
      return;
    }
    this.aggregateType = aggregateType;
  }

  public void setAggregateId(@Nullable String aggregateId) {
    if (StringUtils.isBlank(aggregateId)) {
      this.aggregateId = "";
      return;
    }
    this.aggregateId = aggregateId;
  }

  public void setHeaders(@Nullable String headers) {
    if (StringUtils.isBlank(headers)) {
      this.headers = "{}";
      return;
    }
    this.headers = headers;
  }
}
