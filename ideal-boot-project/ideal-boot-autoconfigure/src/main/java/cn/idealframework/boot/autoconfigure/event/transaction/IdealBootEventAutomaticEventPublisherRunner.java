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

import cn.idealframework.boot.starter.module.event.EventModule;
import cn.idealframework.event.publisher.EventPublisher;
import cn.idealframework.event.publisher.transaction.AutomaticEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;

import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2021/7/1
 */
@ConditionalOnClass({EventPublisher.class, EventModule.class})
public class IdealBootEventAutomaticEventPublisherRunner implements ApplicationRunner {

  @Nullable
  private final AutomaticEventPublisher automaticEventPublisher;

  public IdealBootEventAutomaticEventPublisherRunner(@Nullable @Autowired(required = false)
                                                         AutomaticEventPublisher automaticEventPublisher) {
    this.automaticEventPublisher = automaticEventPublisher;
  }

  @Override
  public void run(ApplicationArguments args) {
    if (automaticEventPublisher != null) {
      automaticEventPublisher.start();
    }
  }
}
