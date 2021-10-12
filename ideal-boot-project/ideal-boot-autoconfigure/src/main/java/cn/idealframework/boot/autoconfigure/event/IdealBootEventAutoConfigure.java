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
package cn.idealframework.boot.autoconfigure.event;

import cn.idealframework.boot.starter.module.event.EventModule;
import cn.idealframework.event.listener.EventDeliverer;
import cn.idealframework.event.listener.EventListenerInitializer;
import cn.idealframework.event.listener.IdempotentHandler;
import cn.idealframework.event.listener.impl.ListenerEventDeliverer;
import cn.idealframework.event.listener.impl.SpringEventListenerInitializer;
import cn.idealframework.util.Asserts;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2021/7/1
 */
@CommonsLog
@RequiredArgsConstructor
@ConditionalOnClass(EventModule.class)
@EnableConfigurationProperties(IdealBootEventProperties.class)
public class IdealBootEventAutoConfigure {

  @Bean
  public EventListenerInitializer eventListenerInitializer() {
    return new SpringEventListenerInitializer();
  }

  @Bean
  @ConditionalOnMissingBean
  public EventDeliverer eventDeliverer(@Nonnull IdempotentHandler idempotentHandler) {
    Asserts.nonnull(idempotentHandler, "IdempotentHandler is null");
    return new ListenerEventDeliverer(idempotentHandler);
  }
}
