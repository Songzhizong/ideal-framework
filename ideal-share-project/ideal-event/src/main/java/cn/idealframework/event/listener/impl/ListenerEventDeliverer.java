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

import cn.idealframework.event.listener.*;
import cn.idealframework.event.message.DeliverEventMessage;
import cn.idealframework.event.message.EventHeaders;
import cn.idealframework.event.message.impl.SimpleEventContext;
import cn.idealframework.json.JacksonUtils;
import cn.idealframework.json.JsonUtils;
import cn.idealframework.lang.Maps;
import cn.idealframework.lang.StringUtils;
import com.fasterxml.jackson.databind.JavaType;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * @author 宋志宗 on 2021/4/23
 */
@CommonsLog
@RequiredArgsConstructor
public class ListenerEventDeliverer implements EventDeliverer {
  @Nonnull
  private final IdempotentHandler idempotentHandler;

  @Override
  public void deliver(@Nonnull DeliverEventMessage message) throws Exception {
    String uuid = message.uuid();
    String topic = message.getTopic();
    if (StringUtils.isAnyBlank(uuid, topic)) {
      log.warn("消息处理失败, 非eventMessage结构: " + JsonUtils.toJsonString(message));
      return;
    }
    Object payload = message.getPayload();
    String payloadString;
    if (payload instanceof String) {
      payloadString = (String) payload;
    } else if (payload instanceof CharSequence) {
      payloadString = ((CharSequence) payload).toString();
    } else {
      payloadString = JsonUtils.toJsonString(payload);
    }
    char c = payloadString.charAt(0);
    if (c != '{') {
      log.warn("消息处理失败, 非json结构");
      return;
    }
    this.handleAll(message, payloadString);
    this.handleRemotes(message, payloadString);
  }

  private void handleAll(@Nonnull DeliverEventMessage message,
                         @Nonnull String payloadString) throws Exception {
    String uuid = message.uuid();
    String topic = message.getTopic();
    EventHeaders headers = message.getHeaders();
    Map<String, IndiscriminateEventProcessor> processorMap = IndiscriminateEventProcessorFactory.getAll();
    if (Maps.isEmpty(processorMap)) {
      return;
    }
    Set<Map.Entry<String, IndiscriminateEventProcessor>> entries = processorMap.entrySet();
    for (Map.Entry<String, IndiscriminateEventProcessor> entry : entries) {
      IndiscriminateEventProcessor processor = entry.getValue();
      if (!processor.match(headers)) {
        continue;
      }
      // 幂等
      String processorName = processor.getName();
      boolean consumed = idempotentHandler.consumed(processorName, uuid);
      if (consumed) {
        log.info("event: " + uuid + " listener: " + processorName + " consumed");
        continue;
      }
      long startTime = System.currentTimeMillis();
      try {
        SimpleEventContext<String> context
          = SimpleEventContext.ofStringPayload(message, payloadString);
        processor.invoke(context);
        if (log.isDebugEnabled()) {
          long consuming = System.currentTimeMillis() - startTime;
          log.debug("Event message deliver to " + processorName
            + ". topic: " + topic + " uuid: " + uuid + " consuming: " + consuming);
        }
      } catch (Exception exception) {
        // 消费失败释放幂等key
        idempotentHandler.remove(processorName, uuid);
        if (log.isDebugEnabled()) {
          long consuming = System.currentTimeMillis() - startTime;
          log.debug("Event message deliver to " + processorName
            + ". topic: " + topic + " uuid: " + uuid
            + " consuming: " + consuming + " throw ex: " + exception.getMessage());
        }
        throw exception;
      }
    }
  }

  private void handleRemotes(@Nonnull DeliverEventMessage message,
                             @Nonnull String payloadString) throws Exception {
    String uuid = message.uuid();
    String topic = message.getTopic();
    String listenerName = message.getListenerName();
    Collection<RemoteEventProcessor> handlers = getRemoteEventProcessor(message, listenerName);
    for (RemoteEventProcessor handler : handlers) {
      // 幂等校验
      String handlerName = handler.getName();
      boolean consumed = idempotentHandler.consumed(handlerName, uuid);
      if (consumed) {
        log.info("event: " + uuid + " listener: " + handlerName + " consumed");
        continue;
      }
      long startTime = System.currentTimeMillis();
      try {
        JavaType payloadType = handler.getPayloadType();
        Object param;
        try {
          param = JacksonUtils.parse(payloadString, payloadType);
        } catch (Exception e) {
          log.warn("反序列化消息出现异常: " + e.getClass().getName() + " " + e.getMessage());
          continue;
        }
        SimpleEventContext<Object> context = SimpleEventContext.of(message, param);
        handler.invoke(context);
        if (log.isDebugEnabled()) {
          long consuming = System.currentTimeMillis() - startTime;
          log.debug("Event message deliver to " + handler.getName()
            + ". topic: " + topic + " uuid: " + uuid + " consuming: " + consuming);
        }
      } catch (Exception exception) {
        // 消费失败释放幂等key
        idempotentHandler.remove(handlerName, uuid);
        if (log.isDebugEnabled()) {
          long consuming = System.currentTimeMillis() - startTime;
          log.debug("Event message deliver to " + handler.getName()
            + ". topic: " + topic + " uuid: " + uuid
            + " consuming: " + consuming + " throw ex: " + exception.getMessage());
        }
        throw exception;
      }
    }
  }

  @Nonnull
  private Collection<RemoteEventProcessor> getRemoteEventProcessor(@Nonnull DeliverEventMessage message,
                                                                   @Nullable String listenerName) {
    String uuid = message.uuid();
    String topic = message.getTopic();
    EventHeaders headers = message.getHeaders();
    Map<String, RemoteEventProcessor> processorMap = RemoteEventProcessorFactory.get(topic);
    if (processorMap.isEmpty()) {
      log.debug("Could not find remote listeners for the event: " + topic);
      return Collections.emptyList();
    }
    if (StringUtils.isBlank(listenerName)) {
      Collection<RemoteEventProcessor> values = processorMap.values();
      AtomicBoolean atomicBoolean = new AtomicBoolean(false);
      List<RemoteEventProcessor> filterHandlers = values.stream().
        filter(handler -> {
          boolean match = handler.match(headers);
          if (!match) {
            atomicBoolean.set(true);
            log.debug("Failed match. listener: " + handler.getName()
              + " topic: " + topic + " event uuid: " + uuid);
          }
          return match;
        })
        .collect(Collectors.toList());
      if (atomicBoolean.get() && log.isDebugEnabled() && headers != null) {
        log.debug("Event headers: " + JsonUtils.toJsonString(headers));
      }
      return filterHandlers;
    }
    RemoteEventProcessor handler = processorMap.get(listenerName);
    if (handler == null) {
      log.error("Listener: \"" + listenerName + "\" not found for the event: " + topic);
      return Collections.emptyList();
    }
    return Collections.singletonList(handler);
  }
}
