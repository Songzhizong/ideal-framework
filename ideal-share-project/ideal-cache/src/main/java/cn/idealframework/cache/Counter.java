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
package cn.idealframework.cache;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 计数器接口
 *
 * @author 宋志宗 on 2021/12/10
 */
public interface Counter {

  /**
   * 获取计数
   *
   * @param key key
   * @return 计数
   * @author 宋志宗 on 2021/12/10
   */
  @Nullable
  Long get(@Nonnull String key);

  /**
   * 设置计数
   *
   * @param key key
   * @return 旧的计数, 如果之前没有则返回null
   * @author 宋志宗 on 2021/12/10
   */
  @Nullable
  Long getAndSet(@Nonnull String key, long value);

  /**
   * 移除一个计数器
   *
   * @param key key
   * @author 宋志宗 on 2021/12/10
   */
  @Nullable
  Long getAndRemove(@Nonnull String key);

  /**
   * 移除一个计数器
   *
   * @param key key
   * @author 宋志宗 on 2021/12/10
   */
  void remove(@Nonnull String key);

  /**
   * 自增1
   *
   * @param key key
   * @return 自增后的计数
   * @author 宋志宗 on 2021/12/10
   */
  default long increment(@Nonnull String key) {
    return increment(key, 1);
  }

  /**
   * 增加一定的数值
   *
   * @param key   key
   * @param delta 增加的值
   * @return 增加后的计数
   * @author 宋志宗 on 2021/12/10
   */
  long increment(@Nonnull String key, long delta);


  /**
   * 自减1
   *
   * @param key key
   * @return 自减后的计数
   * @author 宋志宗 on 2021/12/10
   */
  default long decrement(@Nonnull String key) {
    return decrement(key, 1);
  }


  /**
   * 减少一定的数值
   *
   * @param key   key
   * @param delta 减少的值
   * @return 减少后的计数
   * @author 宋志宗 on 2021/12/10
   */
  long decrement(@Nonnull String key, long delta);
}
