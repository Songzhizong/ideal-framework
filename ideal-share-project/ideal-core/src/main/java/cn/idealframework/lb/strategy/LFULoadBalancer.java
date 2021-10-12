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
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 最不经常使用
 * <p>返回一定时间内使用次数最少的服务</p>
 *
 * @author 宋志宗 on 2020/8/19
 */
public class LFULoadBalancer<Server extends LbServer> implements LoadBalancer<Server> {
  /**
   * key 为空时使用
   * <p>
   * server instanceId -> server 最近选用次数
   */
  private ConcurrentMap<String, AtomicLong> defaultLfuMap = new ConcurrentHashMap<>();
  /**
   * key 不为空时使用
   * <p>
   * key -> server instanceId -> server 最近选用次数
   */
  private ConcurrentMap<Object, ConcurrentMap<String, AtomicLong>> multiLfuMap
      = new ConcurrentHashMap<>();
  private final ScheduledExecutorService scheduled
      = Executors.newSingleThreadScheduledExecutor();

  {
    scheduled.scheduleAtFixedRate(() -> {
      defaultLfuMap = new ConcurrentHashMap<>();
      multiLfuMap = new ConcurrentHashMap<>();
    }, 8, 8, TimeUnit.HOURS);
  }

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

    ConcurrentMap<String, AtomicLong> lfuMap = defaultLfuMap;
    if (key != null) {
      lfuMap = multiLfuMap.computeIfAbsent(key, (k) -> new ConcurrentHashMap<>(servers.size()));
    }

    Server selected = null;
    Long minCount = null;
    int bound = size * 10;
    for (Server server : servers) {
      String instanceId = server.getInstanceId();
      AtomicLong atomicLong = lfuMap.computeIfAbsent(instanceId,
          (k) -> new AtomicLong(ThreadLocalRandom.current().nextLong(bound)));
      long count = atomicLong.get();
      if (minCount == null || count < minCount) {
        minCount = count;
        selected = server;
      }
    }
    return selected;
  }
}
