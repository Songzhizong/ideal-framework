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
package cn.idealframework.boot.autoconfigure.event.persistent;

import cn.idealframework.boot.autoconfigure.event.IdealBootEventProperties;
import cn.idealframework.boot.starter.module.event.EventModule;
import cn.idealframework.event.persistence.EventMessageRepository;
import cn.idealframework.event.persistence.jdbc.JdbcTemplateEventMessageRepository;
import cn.idealframework.event.publisher.EventPublisher;
import cn.idealframework.util.Asserts;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2021/7/1
 */
@CommonsLog
@RequiredArgsConstructor
@EnableConfigurationProperties(IdealBootEventProperties.class)
@ConditionalOnClass({EventPublisher.class, EventModule.class})
@ConditionalOnExpression("${ideal.event.publisher.persistent.enabled:false}")
public class IdealBootEventPersistentAutoConfigure {

  @Bean
  @ConditionalOnMissingBean
  public EventMessageRepository eventMessageStorage(@Nonnull JdbcTemplate jdbcTemplate) {
    Asserts.nonnull(jdbcTemplate, "jdbcTemplate is null");
    return new JdbcTemplateEventMessageRepository(jdbcTemplate);
  }
}
