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
package cn.idealframework.lock.redis.redisson;

import cn.idealframework.lock.DLock;
import cn.idealframework.lock.DLockFactory;
import cn.idealframework.util.Asserts;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import javax.annotation.Nonnull;
import java.time.Duration;

/**
 * @author 宋志宗 on 2021/6/26
 */
@CommonsLog
@RequiredArgsConstructor
public class RedissonDLockFactory implements DLockFactory {
  private static final String BASE_NAME_PREFIX = "ideal:dlock:";

  private final String serviceName;
  private final RedissonClient redissonClient;

  @Nonnull
  @Override
  public DLock getLock(@Nonnull String name) {
    return getLock(name, Duration.ofSeconds(30));
  }

  @Nonnull
  @Override
  public DLock getLock(@Nonnull String name, @Nonnull Duration leaseTime) {
    Asserts.notBlank(name, "Lock name must be not blank");
    String lockName = BASE_NAME_PREFIX + serviceName + ":" + name;
    RLock lock = redissonClient.getLock(lockName);
    return new RedissonDLock(lockName, lock, leaseTime.toMillis());
  }
}
