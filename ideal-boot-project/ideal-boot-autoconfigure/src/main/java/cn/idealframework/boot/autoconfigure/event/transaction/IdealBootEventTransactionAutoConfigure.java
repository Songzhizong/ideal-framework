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
package cn.idealframework.boot.autoconfigure.event.transaction;

import cn.idealframework.boot.autoconfigure.event.IdealBootEventProperties;
import cn.idealframework.boot.autoconfigure.event.broker.properties.IdealBootEventBrokerProperties;
import cn.idealframework.boot.starter.module.event.EventModule;
import cn.idealframework.event.persistence.EventMessageRepository;
import cn.idealframework.event.publisher.EventPublisher;
import cn.idealframework.event.publisher.TransactionalEventPublisher;
import cn.idealframework.event.publisher.transaction.AutomaticEventPublisher;
import cn.idealframework.event.publisher.transaction.jdbc.DatabaseAutomaticEventPublisher;
import cn.idealframework.event.publisher.transaction.jdbc.JdbcTemplateTransactionalEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.sql.DataSource;

/**
 * @author 宋志宗 on 2021/7/1
 */
@CommonsLog
@RequiredArgsConstructor
@EnableConfigurationProperties(IdealBootEventProperties.class)
@ConditionalOnClass({EventPublisher.class, EventModule.class})
@ConditionalOnExpression("${ideal.event.publisher.transaction.enabled:true}")
public class IdealBootEventTransactionAutoConfigure {
  private final IdealBootEventProperties properties;

  @Bean("transactionalEventPublisher")
  public TransactionalEventPublisher transactionalEventPublisher(@Nonnull JdbcTemplate jdbcTemplate,
                                                                 @Nonnull EventPublisher eventPublisher,
                                                                 @Nullable @Autowired(required = false)
                                                                   EventMessageRepository eventMessageRepository) {
    IdealBootEventBrokerProperties.BrokerType type = properties.getBroker().getType();
    if (type == IdealBootEventBrokerProperties.BrokerType.LOCAL
      || type == IdealBootEventBrokerProperties.BrokerType.LOG) {
      log.info("Broker type is log or local, using EventPublisher");
      return eventPublisher::publish;
    }
    log.info("JdbcTemplateTransactionalEventPublisher init");
    return new JdbcTemplateTransactionalEventPublisher(jdbcTemplate, eventMessageRepository);
  }

  @Bean
  @Nullable
  public AutomaticEventPublisher automaticEventPublisher(@Nonnull EventPublisher eventPublisher,
                                                         @Nonnull DataSource dataSource) {
    IdealBootEventBrokerProperties.BrokerType type = properties.getBroker().getType();
    if (type == IdealBootEventBrokerProperties.BrokerType.LOCAL
      || type == IdealBootEventBrokerProperties.BrokerType.LOG) {
      log.info("Broker type is log or local, with no need for AutomaticEventPublisher");
      return null;
    }
    if (!properties.getPublisher().getTransaction().isEnabled()) {
      return null;
    }
    if (eventPublisher instanceof TransactionalEventPublisher) {
      throw new IllegalArgumentException("EventPublisher must be not TransactionalEventPublisher");
    }
    log.info("DatabaseAutomaticEventPublisher init");
    return new DatabaseAutomaticEventPublisher(eventPublisher, dataSource, 500);
  }
}
