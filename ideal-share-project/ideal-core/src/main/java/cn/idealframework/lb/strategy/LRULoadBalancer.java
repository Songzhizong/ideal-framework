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
package cn.idealframework.lb.strategy;

import cn.idealframework.lb.LbServer;
import cn.idealframework.lb.LoadBalancer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 最近最久未使用
 *
 * @author 宋志宗 on 2020/8/19
 */
public class LRULoadBalancer<Server extends LbServer> implements LoadBalancer<Server> {

  private final ConcurrentMap<String, Long> defaultLruMap = new ConcurrentHashMap<>();
  private final ConcurrentMap<Object, ConcurrentMap<String, Long>> multiLruMap
      = new ConcurrentHashMap<>();

  @Override
  @Nullable
  public Server chooseServer(@Nullable Object key,
                             @Nonnull List<Server> servers) {
    if (servers.isEmpty()) {
      return null;
    }
    int size = servers.size();
    if (size == 1) {
      return servers.get(0);
    }

    final long currentTimeMillis = System.currentTimeMillis();
    Server selected = null;
    Long maxDifference = null;
    ConcurrentMap<String, Long> lruMap = defaultLruMap;
    if (key != null) {
      multiLruMap.computeIfAbsent(key, (k) -> new ConcurrentHashMap<>(servers.size()));
    }
    for (Server server : servers) {
      final String instanceId = server.getInstanceId();
      final Long lastSelectTime = lruMap.putIfAbsent(instanceId, 0L);
      assert lastSelectTime != null;
      final long difference = currentTimeMillis - lastSelectTime;
      if (maxDifference == null || difference > maxDifference) {
        maxDifference = difference;
        selected = server;
      }
    }
    if (selected != null) {
      lruMap.put(selected.getInstanceId(), currentTimeMillis);
    }
    return selected;
  }
}
