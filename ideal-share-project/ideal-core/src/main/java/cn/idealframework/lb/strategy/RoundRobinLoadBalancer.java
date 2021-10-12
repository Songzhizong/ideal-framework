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
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询策略
 *
 * @author 宋志宗 on 2020/8/19
 */
public class RoundRobinLoadBalancer<Server extends LbServer> implements LoadBalancer<Server> {
  private final AtomicInteger defaultCounter
      = new AtomicInteger(ThreadLocalRandom.current().nextInt(100));
  private final ConcurrentMap<Object, AtomicInteger> counterMap = new ConcurrentHashMap<>();

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
    AtomicInteger counter = defaultCounter;
    if (key != null) {
      counter = counterMap.computeIfAbsent(key,
          (k) -> new AtomicInteger(ThreadLocalRandom.current().nextInt(100)));
    }
    int abs = Math.abs(counter.incrementAndGet());
    return servers.get(abs % size);
  }
}
