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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 宋志宗 on 2021/4/25
 */
public final class RabbitEventUtils {
  private static final Map<String, String> QUEUE_NAME_MAP = new ConcurrentHashMap<>();

  private RabbitEventUtils() {
  }

  @Nonnull
  public static List<String> getAllQueueName() {
    return new ArrayList<>(QUEUE_NAME_MAP.values());
  }

  @Nonnull
  public static String generateQueueName(@Nonnull String queuePrefix,
                                         @Nonnull String listenerName,
                                         boolean enableLocalModel) {
    String queueName = queuePrefix + listenerName;
    return QUEUE_NAME_MAP.computeIfAbsent(queueName, k -> {
      if (enableLocalModel) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return queueName + "-" + uuid;
      } else {
        return queueName;
      }
    });
  }

  @Nonnull
  public static String getListenerNameByQueueName(@Nonnull String queuePrefix,
                                                  @Nonnull String queueName,
                                                  boolean enableLocalModel) {
    String listenerName = queueName.substring(queuePrefix.length());
    if (enableLocalModel) {
      int lastIndexOf = listenerName.lastIndexOf("-");
      listenerName = listenerName.substring(0, lastIndexOf);
    }
    return listenerName;
  }
}
