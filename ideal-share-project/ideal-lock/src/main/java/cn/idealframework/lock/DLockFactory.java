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
package cn.idealframework.lock;

import javax.annotation.Nonnull;
import java.time.Duration;

/**
 * 分布式锁工厂
 *
 * @author 宋志宗 on 2021/6/26
 */
public interface DLockFactory {

  /**
   * 获取分布式锁实例
   *
   * @param name 锁名称
   * @return 分布式锁实例
   * @author 宋志宗 on 2021/6/26
   */
  @Nonnull
  DLock getLock(@Nonnull String name);

  /**
   * 获取分布式锁实例
   *
   * @param name      锁名称
   * @param leaseTime 持有时间
   * @return 分布式锁实例
   * @author 宋志宗 on 2021/6/26
   */
  @Nonnull
  DLock getLock(@Nonnull String name, @Nonnull Duration leaseTime);
}
