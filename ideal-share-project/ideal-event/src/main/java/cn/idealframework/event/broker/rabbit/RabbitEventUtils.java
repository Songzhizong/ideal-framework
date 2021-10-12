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
package cn.idealframework.event.broker.rabbit;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2021/4/25
 */
public final class RabbitEventUtils {

  private RabbitEventUtils() {
  }

  @Nonnull
  public static String generateQueueName(@Nonnull String queuePrefix, @Nonnull String listenerName) {
    return queuePrefix + listenerName;
  }

  @Nonnull
  public static String getListenerNameByQueueName(@Nonnull String queuePrefix, @Nonnull String queueName) {
    return queueName.substring(queuePrefix.length());
  }
}
