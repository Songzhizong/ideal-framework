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
package cn.idealframework.boot.autoconfigure.event.broker.properties.rabbit;

import lombok.Getter;
import lombok.Setter;

/**
 * @author 宋志宗 on 2021/10/12
 */
@Getter
@Setter
public class IdealBootEventRabbitProperties {
  /** 交换区 */
  private String exchange = "ideal.event.exchange";

  /** 消费队列前缀 */
  private String queuePrefix = "ideal.event.queue";

  /** 消费者预取消息数 */
  private int prefetchCount = 0;

  /** 默认消费者数 */
  private int concurrentConsumers = 16;

  /** 最大消费者数 */
  private int maxConcurrentConsumers = 64;
}
