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
package cn.idealframework.event.publisher;

import cn.idealframework.event.message.DomainEvent;
import cn.idealframework.event.message.EventMessage;
import cn.idealframework.event.persistence.EventMessageRepository;
import cn.idealframework.json.JsonUtils;
import lombok.extern.apachecommons.CommonsLog;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

/**
 * 仅打印日志而不会发布事件的事件发布器
 *
 * @author 宋志宗 on 2021/4/22
 */
@CommonsLog
public class LogOnlyEventPublisher extends AbstractEventPublisher {

  public LogOnlyEventPublisher(@Nullable EventMessageRepository eventMessageRepository) {
    super(eventMessageRepository);
  }

  @Override
  public void directPublish(@Nonnull Collection<EventMessage<? extends DomainEvent>> messages) {
    log.info("Publish event message:\n" + JsonUtils.toPrettyJsonString(messages));
  }
}
