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
import cn.idealframework.event.message.DomainEvent;
import cn.idealframework.event.message.EventHeaders;
import cn.idealframework.event.message.EventMessage;
import cn.idealframework.event.persistence.EventMessageRepository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

/**
 * @author 宋志宗 on 2021/7/23
 */
public abstract class AbstractEventPublisher implements EventPublisher {
  private final EventMessageRepository eventMessageRepository;

  protected AbstractEventPublisher(@Nullable EventMessageRepository eventMessageRepository) {
    this.eventMessageRepository = eventMessageRepository;
  }

  @Override
  public void publish(@Nonnull Collection<EventMessage<? extends DomainEvent>> messages) {
    for (EventMessage<? extends DomainEvent> message : messages) {
      String topic = message.getTopic();
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
    if (eventMessageRepository != null) {
      eventMessageRepository.saveAll(messages);
    }
    this.directPublish(messages);
  }

  abstract public void directPublish(@Nonnull Collection<EventMessage<? extends DomainEvent>> messages);
}
