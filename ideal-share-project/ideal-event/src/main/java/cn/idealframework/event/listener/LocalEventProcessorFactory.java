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

import cn.idealframework.lang.Lists;
import cn.idealframework.util.Asserts;
import lombok.extern.apachecommons.CommonsLog;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 宋志宗 on 2021/4/23
 */
@CommonsLog
public final class LocalEventProcessorFactory {
  /**
   * topic -> listener name -> {@link RemoteEventProcessor}
   */
  private static final Map<String, List<LocalEventProcessor>> LOCAL_PROCESSORS_MAPPING = new HashMap<>();

  public static void register(@Nonnull String topic,
                              @Nonnull String listenerName,
                              @Nonnull LocalEventProcessor processor) {
    Asserts.notBlank(topic, "监听器监听的主题名称不能为空");
    List<LocalEventProcessor> list = LOCAL_PROCESSORS_MAPPING.computeIfAbsent(topic, k -> new ArrayList<>());
    list.add(processor);
    log.info("Register event processor: " + listenerName);
  }

  @Nonnull
  public static List<LocalEventProcessor> get(@Nonnull String topic) {
    List<LocalEventProcessor> localEventProcessors = LOCAL_PROCESSORS_MAPPING.get(topic);
    if (localEventProcessors == null) {
      return Lists.of();
    }
    return localEventProcessors;
  }

  private LocalEventProcessorFactory() {
  }
}
