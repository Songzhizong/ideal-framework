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
package cn.idealframework.event.listener;

import cn.idealframework.event.condition.EventCondition;
import cn.idealframework.event.message.DomainEvent;
import cn.idealframework.event.message.EventMessage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 事件监听器接口
 *
 * @author 宋志宗 on 2021/4/22
 */
public interface LocalEventListener<T extends DomainEvent> {

  /**
   * @return 监听主题
   * @author 宋志宗 on 2021/4/23
   */
  @Nonnull
  String listeningTopic();

  /**
   * @return 监听条件
   * @author 宋志宗 on 2021/4/26
   */
  @Nullable
  default EventCondition condition() {
    return null;
  }

  /**
   * 接收事件
   *
   * @param message 事件消息
   * @author 宋志宗 on 2021/4/23
   */
  void handleEvent(@Nonnull EventMessage<T> message) throws Exception;
}
