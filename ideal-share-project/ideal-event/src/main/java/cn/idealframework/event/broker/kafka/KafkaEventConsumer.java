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

import cn.idealframework.concurrent.BasicThreadFactory;
import cn.idealframework.event.listener.EventDeliverer;
import cn.idealframework.event.message.impl.SimpleDelivererEvent;
import cn.idealframework.json.JsonUtils;
import cn.idealframework.json.TypeReference;
import cn.idealframework.lang.StringUtils;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author 宋志宗 on 2021/4/24
 */
@CommonsLog
public class KafkaEventConsumer {
  private static final TypeReference<SimpleDelivererEvent> MESSAGE_TYPE_REFERENCE
    = new TypeReference<SimpleDelivererEvent>() {
  };
  private final EventDeliverer eventDeliverer;
  private final KafkaConsumer<String, String> kafkaConsumer;
  private final ExecutorService consumerExecutor = Executors.newSingleThreadExecutor();
  private final ThreadPoolExecutor threadPoolExecutor;
  private volatile boolean running;

  public KafkaEventConsumer(@Nonnull EventDeliverer eventDeliverer,
                            @Nonnull ConsumerProperties properties) {
    this.eventDeliverer = eventDeliverer;
    HashMap<String, Object> configs = new HashMap<>();
    configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, String.join(",", properties.getBootstrapServers()));
    configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
    configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    configs.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, properties.getMaximumPoolSize());
    configs.put(ConsumerConfig.CLIENT_ID_CONFIG, properties.getClientId());
    String groupId = properties.getGroupId();
    if (StringUtils.isBlank(groupId)) {
      throw new IllegalArgumentException("Kafka group.id must be not blank");
    }
    configs.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    configs.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 10_000);
    configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    this.kafkaConsumer = new KafkaConsumer<>(configs);
    this.kafkaConsumer.subscribe(properties.getTopics());
    threadPoolExecutor = new ThreadPoolExecutor(properties.getCorePoolSize(),
        properties.getMaximumPoolSize(), 5, TimeUnit.MINUTES, new SynchronousQueue<>(),
        new BasicThreadFactory.Builder().namingPattern("event-pool-%d").build(),
        (r, executor) -> {
          if (!executor.isShutdown()) {
            log.error("Event pool is full");
            r.run();
          }
        });
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      this.running = false;
      consumerExecutor.shutdown();
      threadPoolExecutor.shutdown();
    }));
  }

  public void start() {
    log.info("Kafka event consumer start");
    this.running = true;
    consumerExecutor.submit(() -> {
      while (running) {
        ConsumerRecords<String, String> records = this.kafkaConsumer.poll(Duration.ofSeconds(5));
        int count = records.count();
        if (count > 0) {
          log.info("Poll record count: " + count);
        }
        List<Callable<Boolean>> tasks = new ArrayList<>();
        for (ConsumerRecord<String, String> record : records) {
          String value = record.value();
          SimpleDelivererEvent message = JsonUtils.parse(value, MESSAGE_TYPE_REFERENCE);
          tasks.add(() -> {
            eventDeliverer.deliver(message);
            return true;
          });
        }
        try {
          threadPoolExecutor.invokeAll(tasks);
          this.kafkaConsumer.commitSync();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });
  }
}
