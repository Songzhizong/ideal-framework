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
package cn.idealframework.event.publisher;

import cn.idealframework.event.listener.EventDeliverer;
import cn.idealframework.event.message.DeliverEventMessage;
import cn.idealframework.event.message.EventMessage;
import cn.idealframework.event.message.impl.SimpleDelivererEvent;
import cn.idealframework.event.persistence.EventMessageRepository;
import cn.idealframework.json.JsonUtils;
import cn.idealframework.json.TypeReference;
import lombok.extern.apachecommons.CommonsLog;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

/**
 * 本地事件发布器
 *
 * @author 宋志宗 on 2021/4/22
 */
@CommonsLog
@SuppressWarnings("unused")
public class LocalEventPublisher extends AbstractEventPublisher {
  private static final TypeReference<SimpleDelivererEvent> TYPE_REFERENCE
    = new TypeReference<SimpleDelivererEvent>() {
  };
  private final EventDeliverer eventDeliverer;

  public LocalEventPublisher(@Nonnull EventDeliverer eventDeliverer,
                             @Nullable EventMessageRepository eventMessageRepository) {
    super(eventMessageRepository);
    this.eventDeliverer = eventDeliverer;
  }


  @Override
  public void brokerPublish(@Nonnull Collection<EventMessage<?>> messages) {
    for (EventMessage<?> message : messages) {
      String messageString = JsonUtils.toJsonString(message);
      log.debug("Publish event message: " + messageString);
      DeliverEventMessage deliverMessage = JsonUtils.parse(messageString, TYPE_REFERENCE);
      try {
        eventDeliverer.deliver(deliverMessage);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }
}
