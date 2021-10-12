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
package cn.idealframework.boot.autoconfigure.event.broker.properties;

import cn.idealframework.boot.autoconfigure.event.broker.properties.kafka.IdealBootEventKafkaProperties;
import cn.idealframework.boot.autoconfigure.event.broker.properties.rabbit.IdealBootEventRabbitProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author 宋志宗 on 2021/7/1
 */
@Getter
@Setter
public class IdealBootEventBrokerProperties {
  /** 代理类型, 默认本地代理 */
  private BrokerType type = BrokerType.LOCAL;

  @NestedConfigurationProperty
  private final IdealBootEventKafkaProperties kafka = new IdealBootEventKafkaProperties();

  @NestedConfigurationProperty
  private final IdealBootEventRabbitProperties rabbit = new IdealBootEventRabbitProperties();

  public enum BrokerType {
    /** 不执行事件发布,仅打印日志 */
    LOG,
    /** 本地代理, 发布的事件只有进程内的消费者消费 */
    LOCAL,
    /** 使用rabbitmq作为事件代理 */
    RABBIT,
    /** 使用kafka作为事件代理 */
    KAFKA,
    /** 不指定事件代理, 由开发者自定义代理 */
    CUSTOM,
  }
}
