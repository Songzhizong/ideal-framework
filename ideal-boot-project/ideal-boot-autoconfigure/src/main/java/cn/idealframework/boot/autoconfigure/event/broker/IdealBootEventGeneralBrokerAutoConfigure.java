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
import cn.idealframework.boot.autoconfigure.event.broker.properties.IdealBootEventBrokerProperties;
import cn.idealframework.boot.starter.module.event.EventModule;
import cn.idealframework.event.listener.EventDeliverer;
import cn.idealframework.event.persistence.EventMessageRepository;
import cn.idealframework.event.publisher.EventPublisher;
import cn.idealframework.event.publisher.InMemoryEventPublisher;
import cn.idealframework.event.publisher.LogOnlyEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2021/7/1
 */
@CommonsLog
@RequiredArgsConstructor
@EnableConfigurationProperties(IdealBootEventProperties.class)
@ConditionalOnClass({EventPublisher.class, EventModule.class})
@ConditionalOnExpression(
    "'${ideal.event.broker.type:LOCAL}'.equalsIgnoreCase('LOCAL')" +
        "||'${ideal.event.broker.type:LOCAL}'.equalsIgnoreCase('LOG')" +
        "||'${ideal.event.broker.type:LOCAL}'.equalsIgnoreCase('CUSTOM')"
)
public class IdealBootEventGeneralBrokerAutoConfigure {
  private final IdealBootEventProperties properties;

  @Nullable
  @Bean("eventPublisher")
  @ConditionalOnMissingBean
  public EventPublisher eventPublisher(EventDeliverer eventDeliverer,
                                       @Nullable @Autowired(required = false)
                                           EventMessageRepository eventMessageRepository) {
    IdealBootEventBrokerProperties broker = properties.getBroker();
    IdealBootEventBrokerProperties.BrokerType type = broker.getType();
    if (type == IdealBootEventBrokerProperties.BrokerType.CUSTOM) {
      log.info("Use custom EventPublisher return null");
      return null;
    }
    if (type == IdealBootEventBrokerProperties.BrokerType.LOG) {
      log.info("Use log EventPublisher");
      return new LogOnlyEventPublisher(eventMessageRepository);
    }
    log.info("Use local EventPublisher");
    return new InMemoryEventPublisher(eventDeliverer, eventMessageRepository);
  }
}
