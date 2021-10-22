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

import cn.idealframework.event.listener.EventListenerInitializedListener;
import cn.idealframework.event.listener.RemoteEventProcessor;
import cn.idealframework.event.listener.RemoteEventProcessorFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author 宋志宗 on 2021/4/25
 */
public class RabbitInitializer implements EventListenerInitializedListener {
  public static final List<String> QUEUE_NAMES = new ArrayList<>();
  private final AmqpAdmin amqpAdmin;
  private final String queuePrefix;
  private final String publishExchange;
  private final boolean enableLocalModel;

  public RabbitInitializer(@Nonnull AmqpAdmin amqpAdmin,
                           @Nonnull String queuePrefix,
                           @Nonnull String publishExchange,
                           boolean enableLocalModel) {
    this.enableLocalModel = enableLocalModel;
    if (!queuePrefix.endsWith(".")) {
      queuePrefix = queuePrefix + ".";
    }
    this.amqpAdmin = amqpAdmin;
    this.queuePrefix = queuePrefix;
    this.publishExchange = publishExchange;
  }

  public void initialize() {
    Map<String, Map<String, RemoteEventProcessor>> all = RemoteEventProcessorFactory.getAll();
    all.forEach((topic, map) -> {
      TopicExchange exchange = new TopicExchange(publishExchange);
      amqpAdmin.declareExchange(exchange);
      map.forEach((listenerName, h) -> {
        Queue queue;
        if (enableLocalModel) {
          String uuid = UUID.randomUUID().toString().replace("-", "");
          String queueName = RabbitEventUtils.generateQueueName(queuePrefix, listenerName) + "-" + uuid;
          queue = new Queue(queueName, false, false, true);
          QUEUE_NAMES.add(queueName);
        } else {
          String queueName = RabbitEventUtils.generateQueueName(queuePrefix, listenerName);
          queue = new Queue(queueName);
          QUEUE_NAMES.add(queueName);
        }
        amqpAdmin.declareQueue(queue);
        amqpAdmin.declareBinding(BindingBuilder.bind(queue).to(exchange).with(topic));
      });
    });
  }

  @Override
  public void completed() {
    initialize();
  }
}
