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

import lombok.extern.apachecommons.CommonsLog;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 宋志宗 on 2021/4/23
 */
@CommonsLog
public final class RemoteEventProcessorFactory {
  /**
   * topic -> listener name -> {@link RemoteEventProcessor}
   */
  private static final Map<String, Map<String, RemoteEventProcessor>> REMOTE_PROCESSOR_MAPPING = new HashMap<>();

  /**
   * topic -> listener name -> {@link RemoteEventProcessor}
   *
   * @return All EventHandler
   */
  @Nonnull
  public static Map<String, Map<String, RemoteEventProcessor>> getAll() {
    return REMOTE_PROCESSOR_MAPPING;
  }

  /**
   * Register event processor
   *
   * @param topic        topic
   * @param listenerName listener name
   * @param processor    {@link RemoteEventProcessor}
   */
  public static void register(@Nonnull String topic,
                              @Nonnull String listenerName,
                              @Nonnull RemoteEventProcessor processor) {
    Map<String, RemoteEventProcessor> listenerMap
      = REMOTE_PROCESSOR_MAPPING.computeIfAbsent(topic, k -> new HashMap<>(8));
    RemoteEventProcessor previous = listenerMap.put(listenerName, processor);
    if (previous != null) {
      log.error("监听器名称重复: " + listenerName);
      System.exit(0);
    }
    log.info("Register event processor: " + listenerName);
  }

  /**
   * 获取某个主题所有的监听器,  listener name -> {@link RemoteEventProcessor}
   *
   * @param topic 事件主题
   * @return 监听该主题的所有监听器, key为监听器名称
   */
  @Nonnull
  public static Map<String, RemoteEventProcessor> get(@Nonnull String topic) {
    Map<String, RemoteEventProcessor> map = REMOTE_PROCESSOR_MAPPING.get(topic);
    return map == null ? Collections.emptyMap() : map;
  }

  private RemoteEventProcessorFactory() {
  }
}
