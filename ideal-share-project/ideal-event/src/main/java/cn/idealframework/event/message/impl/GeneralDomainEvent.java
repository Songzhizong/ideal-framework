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

import cn.idealframework.event.message.DomainEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.beans.Transient;
import java.util.LinkedHashMap;

/**
 * @author 宋志宗 on 2021/5/1
 */
public class GeneralDomainEvent extends LinkedHashMap<String, Object> implements DomainEvent {
  private static final long serialVersionUID = 362498820763181265L;

  @Nonnull
  @Override
  @Transient
  public String getTopic() {
    Object topic = this.get("topic");
    if (topic == null) {
      return "";
    }
    return topic.toString();
  }

  @Nullable
  @Override
  @Transient
  public String getAggregateType() {
    Object aggregateType = this.get("aggregateType");
    if (aggregateType == null) {
      return "";
    }
    return aggregateType.toString();
  }

  @Nullable
  @Override
  @Transient
  public String getAggregateId() {
    Object aggregateId = this.get("aggregateId");
    if (aggregateId == null) {
      return "";
    }
    return aggregateId.toString();
  }
}
