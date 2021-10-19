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
package cn.idealframework.event.broker.kafka;

import cn.idealframework.event.message.EventMessage;
import cn.idealframework.event.persistence.EventMessageRepository;
import cn.idealframework.event.publisher.AbstractEventPublisher;
import cn.idealframework.json.JsonUtils;
import cn.idealframework.lang.StringUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * @author 宋志宗 on 2021/4/24
 */
@SuppressWarnings("DuplicatedCode")
public class KafkaEventPublisher extends AbstractEventPublisher {
  /** 默认topic, 默认情况下所有的事件消息都将投到kafka中的该topic */
  private final String defaultEventTopic;
  private final KafkaProducer<String, String> kafkaProducer;

  /** 事件主题和kafka主题的映射关系, 通过此映射关系可以按照规则将事件投放到不同的topic */
  @Nonnull
  private final Map<String, String> customTopicMapping;
  private final boolean customTopicMappingIsEmpty;

  public KafkaEventPublisher(@Nonnull PublisherProperties properties,
                             @Nullable EventMessageRepository eventMessageRepository) {
    super(eventMessageRepository);
    String defaultEventTopic = properties.getDefaultEventTopic();
    if (StringUtils.isBlank(defaultEventTopic)) {
      throw new IllegalArgumentException("ideal.event.kafka.default-event-topic must be not blank");
    }
    this.defaultEventTopic = defaultEventTopic;
    this.customTopicMapping = Collections.emptyMap();
    this.customTopicMappingIsEmpty = true;
    HashMap<String, Object> configs = new HashMap<>();
    Set<String> bootstrapServers = properties.getBootstrapServers();
    configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, String.join(",", bootstrapServers));
    configs.put(ProducerConfig.CLIENT_ID_CONFIG, properties.getClientId());
    configs.put(ProducerConfig.ACKS_CONFIG, properties.getAcks());
    configs.put(ProducerConfig.BATCH_SIZE_CONFIG, Math.toIntExact(properties.getBatchSize()));
    configs.put(ProducerConfig.LINGER_MS_CONFIG, Math.toIntExact(properties.getLingerMs().toMillis()));
    configs.put(ProducerConfig.BUFFER_MEMORY_CONFIG, properties.getBufferMemory());
    configs.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, properties.getCompressionType());
    configs.put(ProducerConfig.RETRIES_CONFIG, properties.getRetries());
    configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    this.kafkaProducer = new KafkaProducer<>(configs);
  }

  @Override
  public void brokerPublish(@Nonnull Collection<EventMessage<?>> messages) {
    for (EventMessage<?> message : messages) {
      String entityId = message.getAggregateId();
      String messageStr = JsonUtils.toJsonStringIgnoreNull(message);
      String kafkaTopic = defaultEventTopic;
      if (!customTopicMappingIsEmpty) {
        String eventTopic = message.getTopic();
        String mappedTopic = customTopicMapping.get(eventTopic);
        if (StringUtils.isNotBlank(mappedTopic)) {
          kafkaTopic = mappedTopic;
        }
      }
      ProducerRecord<String, String> record = new ProducerRecord<>(kafkaTopic, entityId, messageStr);
      kafkaProducer.send(record);
    }
  }
}
