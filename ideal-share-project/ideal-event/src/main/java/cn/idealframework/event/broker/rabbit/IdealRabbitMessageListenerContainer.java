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

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;

/**
 * @author 宋志宗 on 2021/4/25
 */
@CommonsLog
public class IdealRabbitMessageListenerContainer extends SimpleMessageListenerContainer {

  public IdealRabbitMessageListenerContainer(RabbitConsumer rabbitConsumer,
                                             ConnectionFactory connectionFactory,
                                             int concurrentConsumers,
                                             int maxConcurrentConsumers,
                                             int prefetchCount) {
    super(connectionFactory);
    if (maxConcurrentConsumers < concurrentConsumers) {
      maxConcurrentConsumers = concurrentConsumers;
    }
    if (prefetchCount < 0) {
      prefetchCount = 0;
    }
    this.setAcknowledgeMode(AcknowledgeMode.MANUAL);
    this.setMessageListener(rabbitConsumer);
    this.setConcurrentConsumers(concurrentConsumers);
    this.setMaxConcurrentConsumers(maxConcurrentConsumers);
    this.setPrefetchCount(prefetchCount);
  }
}
