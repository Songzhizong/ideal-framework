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
import cn.idealframework.event.message.EventMessageBuilder;
import cn.idealframework.lang.CollectionUtils;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 事件发布器接口
 *
 * @author 宋志宗 on 2021/4/22
 */
public interface EventPublisher {

  /**
   * 批量发布事件消息
   *
   * @param messages 事件信息列表
   * @author 宋志宗 on 2021/4/22
   */
  void publish(@Nonnull Collection<EventMessage<? extends DomainEvent>> messages);


  /**
   * 批量发布事件消息构建器
   *
   * @param eventsBuilders 事件消息构建器列表
   * @author 宋志宗 on 2021/4/27
   */
  default void publishAll(@Nonnull Collection<EventMessageBuilder> eventsBuilders) {
    if (CollectionUtils.isEmpty(eventsBuilders)) {
      return;
    }
    List<EventMessage<? extends DomainEvent>> messages = eventsBuilders.stream()
      .map(EventMessageBuilder::build).collect(Collectors.toList());
    publish(messages);
  }
}
