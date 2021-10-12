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
package cn.idealframework.boot.autoconfigure.event.idempotent;

import cn.idealframework.boot.autoconfigure.event.IdealBootEventProperties;
import cn.idealframework.boot.autoconfigure.event.idempotent.properties.IdealBootEventCaffeineIdempotentProperties;
import cn.idealframework.boot.autoconfigure.event.idempotent.properties.IdealBootEventIdempotentProperties;
import cn.idealframework.boot.starter.module.event.EventModule;
import cn.idealframework.event.listener.IdempotentHandler;
import cn.idealframework.event.listener.impl.CaffeineIdempotentHandler;
import cn.idealframework.event.publisher.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2021/7/1
 */
@CommonsLog
@RequiredArgsConstructor
@EnableConfigurationProperties(IdealBootEventProperties.class)
@ConditionalOnClass({EventPublisher.class, EventModule.class})
@ConditionalOnExpression("!'${ideal.event.consumer.idempotent.type:REDIS}'.equalsIgnoreCase('REDIS')")
public class IdealBootEventGeneralIdempotentAutoConfigure {
  private final IdealBootEventProperties properties;

  @Bean
  @ConditionalOnMissingBean
  public IdempotentHandler idempotentHandler() {
    IdealBootEventIdempotentProperties idempotent = properties.getIdempotent();
    IdealBootEventIdempotentProperties.IdempotentType type = idempotent.getType();
    if (type == IdealBootEventIdempotentProperties.IdempotentType.MEMORY) {
      IdealBootEventCaffeineIdempotentProperties caffeine = idempotent.getMemory();
      log.info("Use CaffeineIdempotentHandler");
      return new CaffeineIdempotentHandler(caffeine.getMaximumSize(), caffeine.getTimeout());
    }
    if (type == IdealBootEventIdempotentProperties.IdempotentType.NONE) {
      log.warn("IdempotentHandler is not enabled");
      return new IdempotentHandler() {
        @Override
        public boolean consumed(@Nonnull String handlerName, @Nonnull String uuid) {
          return false;
        }

        @Override
        public void remove(@Nonnull String handlerName, @Nonnull String uuid) {

        }
      };
    }
    return null;
  }

}
