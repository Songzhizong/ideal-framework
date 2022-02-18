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

import cn.idealframework.util.Asserts;
import lombok.extern.apachecommons.CommonsLog;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 宋志宗 on 2022/2/18
 */
@CommonsLog
public final class IndiscriminateEventProcessorFactory {
  /**
   * listener name -> {@link RemoteEventProcessor}
   */
  private static final Map<String, IndiscriminateEventProcessor> REMOTE_PROCESSOR_MAPPING = new HashMap<>();

  @Nonnull
  public static Map<String, IndiscriminateEventProcessor> getAll() {
    return REMOTE_PROCESSOR_MAPPING;
  }

  public static void register(@Nonnull String listenerName,
                              @Nonnull IndiscriminateEventProcessor processor) {
    Asserts.notBlank(listenerName, "远程监听器名称不能为空");
    IndiscriminateEventProcessor previous = REMOTE_PROCESSOR_MAPPING.put(listenerName, processor);
    if (previous != null) {
      log.error("监听器名称重复: " + listenerName);
      System.exit(0);
    }
    log.info("Register all event processor: " + listenerName);
  }
}
