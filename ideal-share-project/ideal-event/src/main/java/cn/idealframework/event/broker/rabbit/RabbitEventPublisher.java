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
package cn.idealframework.event.broker.rabbit;

import cn.idealframework.event.message.EventMessage;
import cn.idealframework.event.persistence.EventMessageRepository;
import cn.idealframework.event.publisher.AbstractEventPublisher;
import cn.idealframework.json.JsonUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

/**
 * @author 宋志宗 on 2021/4/24
 */
public class RabbitEventPublisher extends AbstractEventPublisher {
  private final String publishExchange;
  private final AmqpTemplate amqpTemplate;

  public RabbitEventPublisher(@Nonnull String publishExchange,
                              @Nonnull AmqpTemplate amqpTemplate,
                              @Nullable EventMessageRepository eventMessageRepository) {
    super(eventMessageRepository);
    this.amqpTemplate = amqpTemplate;
    this.publishExchange = publishExchange;
  }

  @Override
  public void brokerPublish(@Nonnull Collection<EventMessage<?>> messages) {
    for (EventMessage<?> eventMessage : messages) {
      String topic = eventMessage.getTopic();
      String jsonString = JsonUtils.toJsonStringIgnoreNull(eventMessage);
      Message message = MessageBuilder.withBody(jsonString.getBytes(StandardCharsets.UTF_8)).build();
      message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
      amqpTemplate.send(publishExchange, topic, message);
    }
  }
}
