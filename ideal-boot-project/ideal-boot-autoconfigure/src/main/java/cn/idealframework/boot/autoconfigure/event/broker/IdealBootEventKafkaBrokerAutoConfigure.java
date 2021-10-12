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
import cn.idealframework.boot.autoconfigure.event.broker.properties.kafka.IdealBootEventKafkaProducerProperties;
import cn.idealframework.boot.autoconfigure.event.broker.properties.kafka.IdealBootEventKafkaProperties;
import cn.idealframework.boot.autoconfigure.event.broker.properties.kafka.IdealBootEventKafkaReceiveProperties;
import cn.idealframework.boot.starter.module.event.EventModule;
import cn.idealframework.event.broker.kafka.ConsumerProperties;
import cn.idealframework.event.broker.kafka.KafkaEventConsumer;
import cn.idealframework.event.broker.kafka.KafkaEventPublisher;
import cn.idealframework.event.broker.kafka.PublisherProperties;
import cn.idealframework.event.listener.EventDeliverer;
import cn.idealframework.event.persistence.EventMessageRepository;
import cn.idealframework.event.publisher.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;

/**
 * @author 宋志宗 on 2021/7/1
 */
@CommonsLog
@RequiredArgsConstructor
@EnableConfigurationProperties(IdealBootEventProperties.class)
@ConditionalOnClass({EventPublisher.class, EventModule.class})
@ConditionalOnExpression("'${ideal.event.broker.type:LOCAL}'.equalsIgnoreCase('KAFKA')")
public class IdealBootEventKafkaBrokerAutoConfigure implements ApplicationRunner {
  private final IdealBootEventProperties properties;
  @SuppressWarnings("SpringJavaAutowiredMembersInspection")
  @Autowired
  private KafkaEventConsumer kafkaEventConsumer;

  @Value("${spring.application.name}")
  private String applicationName;


  @Bean("eventPublisher")
  public EventPublisher eventPublisher(@Nullable @Autowired(required = false)
                                         EventMessageRepository eventMessageRepository) {
    IdealBootEventKafkaProperties kafka = properties.getBroker().getKafka();
    IdealBootEventKafkaProducerProperties producer = kafka.getProducer();
    PublisherProperties publisherProperties = PublisherProperties.builder()
      .defaultEventTopic(producer.getDefaultEventTopic())
      .bootstrapServers(new HashSet<>(kafka.getBootstrapServers()))
      .acks(producer.getAcks())
      .clientId(producer.getAcks())
      .batchSize(producer.getBatchSize().toBytes())
      .lingerMs(producer.getLingerMs())
      .bufferMemory(producer.getBufferMemory().toBytes())
      .compressionType(producer.getCompressionType())
      .retries(producer.getRetries())
      .build();
    return new KafkaEventPublisher(publisherProperties, eventMessageRepository);
  }

  @Bean
  public KafkaEventConsumer kafkaEventConsumer(@Nonnull @Autowired(required = false)
                                                 EventDeliverer eventDeliverer) {
    IdealBootEventKafkaProperties kafka = properties.getBroker().getKafka();
    IdealBootEventKafkaReceiveProperties kafkaReceive = kafka.getReceive();
    int corePoolSize = kafkaReceive.getCorePoolSize();
    int maximumPoolSize = kafkaReceive.getMaximumPoolSize();
    ConsumerProperties properties = ConsumerProperties.builder()
      .groupId(applicationName)
      .clientId(kafka.getClientId())
      .topics(kafkaReceive.getTopics())
      .bootstrapServers(new HashSet<>(kafka.getBootstrapServers()))
      .corePoolSize(corePoolSize)
      .maximumPoolSize(maximumPoolSize)
      .build();
    return new KafkaEventConsumer(eventDeliverer, properties);
  }

  @Override
  public void run(ApplicationArguments args) {
    kafkaEventConsumer.start();
  }
}
