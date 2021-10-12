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
package cn.idealframework.boot.autoconfigure.event.broker;

import cn.idealframework.boot.autoconfigure.event.IdealBootEventProperties;
import cn.idealframework.boot.autoconfigure.event.broker.properties.rabbit.IdealBootEventRabbitProperties;
import cn.idealframework.boot.starter.module.event.EventModule;
import cn.idealframework.event.broker.rabbit.*;
import cn.idealframework.event.listener.EventDeliverer;
import cn.idealframework.event.listener.EventListenerInitializer;
import cn.idealframework.event.listener.RemoteEventProcessor;
import cn.idealframework.event.listener.RemoteEventProcessorFactory;
import cn.idealframework.event.persistence.EventMessageRepository;
import cn.idealframework.event.publisher.EventPublisher;
import cn.idealframework.json.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 宋志宗 on 2021/7/1
 */
@CommonsLog
@RequiredArgsConstructor
@EnableConfigurationProperties(IdealBootEventProperties.class)
@ConditionalOnClass({EventPublisher.class, EventModule.class})
@ConditionalOnExpression("'${ideal.event.broker.type:LOCAL}'.equalsIgnoreCase('RABBIT')")
public class IdealBootEventRabbitBrokerAutoConfigure implements ApplicationRunner {
  private final IdealBootEventProperties properties;
  private final EventListenerInitializer eventListenerInitializer;

  @SuppressWarnings("SpringJavaAutowiredMembersInspection")
  @Autowired
  private IdealRabbitMessageListenerContainer listenerContainer;

  @Bean
  public RabbitInitializer rabbitInitializer(AmqpAdmin amqpAdmin) {
    IdealBootEventRabbitProperties rabbit = properties.getBroker().getRabbit();
    String queuePrefix = rabbit.getQueuePrefix();
    String exchange = rabbit.getExchange();
    RabbitInitializer rabbitInitializer = new RabbitInitializer(amqpAdmin, queuePrefix, exchange);
    eventListenerInitializer.addCompleteListener(rabbitInitializer);
    return rabbitInitializer;
  }

  @Bean("eventPublisher")
  public EventPublisher eventPublisher(@Nonnull AmqpTemplate amqpTemplate,
                                       @Nullable @Autowired(required = false)
                                         EventMessageRepository eventMessageRepository) {
    IdealBootEventRabbitProperties rabbit = properties.getBroker().getRabbit();
    String exchange = rabbit.getExchange();
    return new RabbitEventPublisher(exchange, amqpTemplate, eventMessageRepository);
  }

  @Bean
  public RabbitConsumer rabbitConsumer(EventDeliverer eventDeliverer) {
    IdealBootEventRabbitProperties rabbit = properties.getBroker().getRabbit();
    String queuePrefix = rabbit.getQueuePrefix();
    return new RabbitConsumer(queuePrefix, eventDeliverer);
  }

  @Bean
  public IdealRabbitMessageListenerContainer idealRabbitMessageListenerContainer(
    @Nonnull ConnectionFactory connectionFactory,
    @Nonnull RabbitConsumer rabbitConsumer) {
    IdealBootEventRabbitProperties rabbit = properties.getBroker().getRabbit();
    int prefetchCount = rabbit.getPrefetchCount();
    int concurrentConsumers = rabbit.getConcurrentConsumers();
    int maxConcurrentConsumers = rabbit.getMaxConcurrentConsumers();
    return new IdealRabbitMessageListenerContainer(rabbitConsumer,
      connectionFactory, concurrentConsumers, maxConcurrentConsumers, prefetchCount);
  }

  @Override
  public void run(ApplicationArguments args) {
    IdealBootEventRabbitProperties rabbit = properties.getBroker().getRabbit();
    String queuePrefix = rabbit.getQueuePrefix();
    if (!queuePrefix.endsWith(".")) {
      queuePrefix = queuePrefix + ".";
    }
    Map<String, Map<String, RemoteEventProcessor>> all = RemoteEventProcessorFactory.getAll();
    List<String> queueNames = new ArrayList<>();
    String finalQueuePrefix = queuePrefix;
    all.forEach((t, m) -> m.forEach((n, h) -> queueNames.add(RabbitEventUtils.generateQueueName(finalQueuePrefix, n))));
    log.info("Listen queues: " + JsonUtils.toJsonString(queueNames));
    listenerContainer.addQueueNames(queueNames.toArray(new String[0]));
  }
}
