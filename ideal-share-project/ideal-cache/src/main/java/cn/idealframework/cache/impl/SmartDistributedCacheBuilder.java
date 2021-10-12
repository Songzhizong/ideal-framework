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
package cn.idealframework.cache.impl;

import cn.idealframework.cache.DistributedCacheBuilder;
import cn.idealframework.cache.DistributedCacheType;
import lombok.Setter;
import lombok.extern.apachecommons.CommonsLog;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2021/7/11
 */
@CommonsLog
public final class SmartDistributedCacheBuilder {
  @Setter
  private static DistributedCacheType distributedCacheType = DistributedCacheType.REDIS;

  @Nonnull
  public static <V> DistributedCacheBuilder<V> newBuilder() {
    if (distributedCacheType == DistributedCacheType.REDIS) {
      log.debug("use " + distributedCacheType + " cache");
      return new RedisCacheBuilder<>();
    }
    throw new IllegalArgumentException("Invalid distributedCacheType: " + distributedCacheType);
  }

  private SmartDistributedCacheBuilder() {
  }
}
