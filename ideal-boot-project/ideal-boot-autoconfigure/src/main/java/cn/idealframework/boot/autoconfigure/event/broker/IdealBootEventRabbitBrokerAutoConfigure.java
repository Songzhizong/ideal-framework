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
import cn.idealframework.compression.CompressType;
import cn.idealframework.event.broker.rabbit.IdealRabbitMessageListenerContainer;
import cn.idealframework.event.broker.rabbit.RabbitConsumer;
import cn.idealframework.event.broker.rabbit.RabbitEventPublisher;
import cn.idealframework.event.broker.rabbit.RabbitInitializer;
import cn.idealframework.event.listener.EventDeliverer;
import cn.idealframework.event.listener.EventListenerInitializer;
import cn.idealframework.event.persistence.EventMessageRepository;
import cn.idealframework.event.publisher.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2021/7/1
 */
@CommonsLog
@RequiredArgsConstructor
@EnableConfigurationProperties(IdealBootEventProperties.class)
@ConditionalOnClass({EventPublisher.class, EventModule.class})
@ConditionalOnExpression("'${ideal.event.broker.type:LOCAL}'.equalsIgnoreCase('RABBIT')")
public class IdealBootEventRabbitBrokerAutoConfigure {
  private final IdealBootEventProperties properties;
  private final EventListenerInitializer eventListenerInitializer;

  @Bean
  public RabbitInitializer rabbitInitializer(AmqpAdmin amqpAdmin) {
    IdealBootEventRabbitProperties rabbit = properties.getBroker().getRabbit();
    String queuePrefix = rabbit.getQueuePrefix();
    String exchange = rabbit.getExchange();
    RabbitInitializer rabbitInitializer = new RabbitInitializer(amqpAdmin, queuePrefix, exchange, rabbit.isEnableLocalModel());
    eventListenerInitializer.addCompleteListener(rabbitInitializer);
    return rabbitInitializer;
  }

  @Bean("eventPublisher")
  public EventPublisher eventPublisher(@Nonnull AmqpTemplate amqpTemplate,
                                       @Nullable @Autowired(required = false)
                                         EventMessageRepository eventMessageRepository) {
    IdealBootEventRabbitProperties rabbit = properties.getBroker().getRabbit();
    String exchange = rabbit.getExchange();
    CompressType compressType = rabbit.getCompressType();
    return new RabbitEventPublisher(exchange, amqpTemplate, compressType, eventMessageRepository);
  }

  @Bean
  public RabbitConsumer rabbitConsumer(EventDeliverer eventDeliverer) {
    IdealBootEventRabbitProperties rabbit = properties.getBroker().getRabbit();
    String queuePrefix = rabbit.getQueuePrefix();
    return new RabbitConsumer(queuePrefix, eventDeliverer, rabbit.isEnableLocalModel());
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
}
