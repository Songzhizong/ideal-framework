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

import cn.idealframework.event.message.DeliverEventMessage;

import javax.annotation.Nonnull;

/**
 * 事件交付器
 *
 * @author 宋志宗 on 2021/4/23
 */
public interface EventDeliverer {
  /**
   * 交付事件给监听器
   *
   * @param event 交付的事件消息
   * @author 宋志宗 on 2021/4/23
   */
  void deliver(@Nonnull DeliverEventMessage event) throws Exception;
}
