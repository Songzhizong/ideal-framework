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
package cn.idealframework.event.persistence;

import cn.idealframework.event.message.DomainEvent;
import cn.idealframework.event.message.EventMessage;

import javax.annotation.Nonnull;
import java.util.Collection;

/**
 * 消息存储器
 *
 * @author 宋志宗 on 2021/4/27
 */
public interface EventMessageRepository {

  /**
   * 批量保存
   *
   * @param messages 事件消息列表
   * @author 宋志宗 on 2021/7/1
   */
  void saveAll(@Nonnull Collection<EventMessage<? extends DomainEvent>> messages);

  /**
   * 按topic删除过期的消息
   *
   * @param topic           topic
   * @param eventTimeBefore 在此事件之前
   * @return 删除的条数
   */
  int deleteByTopicAndEventTimeBefore(@Nonnull String topic, long eventTimeBefore);
}
