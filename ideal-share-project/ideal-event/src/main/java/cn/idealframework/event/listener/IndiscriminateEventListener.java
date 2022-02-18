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
import cn.idealframework.event.message.EventContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 监听所有事件
 *
 * @author 宋志宗 on 2022/2/18
 */
public interface IndiscriminateEventListener {
  /**
   * @return 监听器名称, 全局唯一
   * @author 宋志宗 on 2021/4/23
   */
  @Nonnull
  String listenerName();

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
   * @param context 事件上下文
   * @author 宋志宗 on 2021/4/23
   */
  void handleEvent(@Nonnull EventContext<String> context) throws Exception;
}
