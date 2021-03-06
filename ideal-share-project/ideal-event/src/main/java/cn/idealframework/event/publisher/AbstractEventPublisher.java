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

import cn.idealframework.event.listener.LocalEventProcessor;
import cn.idealframework.event.listener.LocalEventProcessorFactory;
import cn.idealframework.event.message.EventHeaders;
import cn.idealframework.event.message.EventMessage;
import cn.idealframework.event.message.EventSupplier;
import cn.idealframework.event.persistence.EventMessageRepository;
import cn.idealframework.lang.CollectionUtils;
import lombok.extern.apachecommons.CommonsLog;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author 宋志宗 on 2021/7/23
 */
@CommonsLog
public abstract class AbstractEventPublisher implements EventPublisher {
  private final EventMessageRepository eventMessageRepository;

  protected AbstractEventPublisher(@Nullable EventMessageRepository eventMessageRepository) {
    this.eventMessageRepository = eventMessageRepository;
  }

  @Override
  public void publish(@Nonnull Collection<EventSupplier> suppliers) {
    if (CollectionUtils.isEmpty(suppliers)) {
      return;
    }
    List<EventMessage<?>> messages = localPublish(suppliers);
    if (eventMessageRepository != null) {
      eventMessageRepository.saveAll(messages);
    }
    this.brokerPublish(messages);
  }

  @Nonnull
  private List<EventMessage<?>> localPublish(@Nonnull Collection<EventSupplier> suppliers) {
    List<EventMessage<?>> messages = new ArrayList<>(suppliers.size());
    StringBuilder sb = new StringBuilder();
    for (EventSupplier supplier : suppliers) {
      EventMessage<?> message = supplier.getEventMessage();
      messages.add(message);
      String topic = message.getTopic();
      if (log.isInfoEnabled()) {
        sb.append(topic).append(" ");
      }
      EventHeaders headers = message.getHeaders();
      List<LocalEventProcessor> list = LocalEventProcessorFactory.get(topic);
      for (LocalEventProcessor processor : list) {
        if (processor.match(headers)) {
          try {
            processor.invoke(message);
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        }
      }
    }
    if (log.isInfoEnabled()) {
      sb.deleteCharAt(sb.length() - 1);
      String topics = sb.toString();
      log.info("发布事件: " + topics);
    }
    return messages;
  }

  abstract public void brokerPublish(@Nonnull Collection<EventMessage<?>> messages);
}
