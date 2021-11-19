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

import cn.idealframework.event.broker.rabbit.IdealRabbitMessageListenerContainer;
import cn.idealframework.event.broker.rabbit.RabbitEventUtils;
import cn.idealframework.json.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;

/**
 * @author 宋志宗 on 2021/7/1
 */
@CommonsLog
@RequiredArgsConstructor
@ConditionalOnExpression("'${ideal.event.broker.type:LOCAL}'.equalsIgnoreCase('RABBIT')")
public class IdealBootEventRabbitBrokerRunner implements ApplicationRunner {
  private final IdealRabbitMessageListenerContainer listenerContainer;

  @Override
  public void run(ApplicationArguments args) {
    String[] queues = RabbitEventUtils.getAllQueueName().toArray(new String[0]);
    log.info("Listen queues: " + JsonUtils.toJsonString(queues));
    listenerContainer.addQueueNames(queues);
  }
}
