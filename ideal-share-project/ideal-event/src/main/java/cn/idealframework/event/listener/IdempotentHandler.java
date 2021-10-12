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

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2021/7/1
 */
public interface IdempotentHandler {

  /**
   * 校验消息是否已被成功消费
   *
   * @param handlerName 消费者名称
   * @param uuid        消息唯一id
   * @return 如果消息已经被消费者成功消费则返回true, 反之返回false
   */
  boolean consumed(@Nonnull String handlerName, @Nonnull String uuid);

  /**
   * 清除标记
   *
   * @param handlerName 消费者名称
   * @param uuid        消息唯一id
   */
  void remove(@Nonnull String handlerName, @Nonnull String uuid);
}
