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
package cn.idealframework.event.listener.impl;

import cn.idealframework.event.condition.EventCondition;
import cn.idealframework.event.listener.RemoteEventListener;
import cn.idealframework.event.listener.RemoteEventProcessor;
import cn.idealframework.event.message.EventContext;
import cn.idealframework.event.message.EventHeaders;
import com.fasterxml.jackson.databind.JavaType;
import lombok.extern.apachecommons.CommonsLog;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2021/4/23
 */
@CommonsLog
public class ClassRemoteEventProcessor implements RemoteEventProcessor {
  @Nonnull
  private final String name;
  @Nonnull
  private final JavaType payloadType;
  @Nonnull
  private final EventCondition condition;
  @Nonnull
  private final RemoteEventListener<Object> eventListener;

  public ClassRemoteEventProcessor(@Nonnull String name,
                                   @Nonnull JavaType payloadType,
                                   @Nonnull EventCondition condition,
                                   @Nonnull RemoteEventListener<Object> eventListener) {
    this.name = name;
    this.payloadType = payloadType;
    this.condition = condition;
    this.eventListener = eventListener;
  }

  @Nonnull
  @Override
  public String getName() {
    return name;
  }

  @Nonnull
  @Override
  public JavaType getPayloadType() {
    return payloadType;
  }

  @Nonnull
  @Override
  public EventCondition getCondition() {
    return condition;
  }

  @Override
  public boolean match(@Nullable EventHeaders headers) {
    boolean match = condition.match(headers);
    if (!match && log.isDebugEnabled()) {
      log.debug("Failed match. listener: " + name + " expression: " + condition.toExpression());
    }
    return match;
  }

  @Override
  public void invoke(@Nonnull EventContext<Object> eventContext) throws Exception {
    eventListener.handleEvent(eventContext);
  }
}
