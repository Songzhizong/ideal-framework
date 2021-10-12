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
import org.redisson.api.RLock;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

/**
 * @author 宋志宗 on 2021/6/26
 */
public class RedissonDLock implements DLock {
  @Nonnull
  private final String name;
  @Nonnull
  private final RLock lock;
  private final long leaseTimeMillis;

  public RedissonDLock(@Nonnull String name,
                       @Nonnull RLock lock,
                       long leaseTimeMillis) {
    this.name = name;
    this.lock = lock;
    this.leaseTimeMillis = leaseTimeMillis;
  }

  @Nonnull
  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean tryLock(@Nonnull Duration waitTime) throws InterruptedException {
    long waitTimeMillis = waitTime.toMillis();
    return lock.tryLock(waitTimeMillis, leaseTimeMillis, TimeUnit.MILLISECONDS);
  }

  @Override
  public void forceUnlock() {
    lock.forceUnlock();
  }

  @Override
  public void lockInterruptibly() throws InterruptedException {
    lock.lockInterruptibly(leaseTimeMillis, TimeUnit.MILLISECONDS);
  }

  @Override
  public Condition newCondition() {
    return lock.newCondition();
  }

  @Override
  public void lock() {
    lock.lock(leaseTimeMillis, TimeUnit.MILLISECONDS);
  }

  @Override
  public boolean tryLock() {
    return lock.tryLock();
  }

  @Override
  public void unlock() {
    lock.unlock();
  }
}
