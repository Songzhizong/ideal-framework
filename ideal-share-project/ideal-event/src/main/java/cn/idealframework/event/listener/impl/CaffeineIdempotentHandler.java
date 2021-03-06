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
package cn.idealframework.event.listener.impl;

import cn.idealframework.event.listener.IdempotentHandler;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import javax.annotation.Nonnull;
import java.time.Duration;

/**
 * @author 宋志宗 on 2021/7/1
 */
public class CaffeineIdempotentHandler implements IdempotentHandler {
  private final Cache<String, Boolean> cache;

  public CaffeineIdempotentHandler(int maximumSize, @Nonnull Duration timeout) {
    cache = Caffeine.newBuilder()
      .maximumSize(maximumSize)
      .expireAfterAccess(timeout)
      .initialCapacity(Math.min(1000, maximumSize))
      .build();
  }

  @Override
  public boolean consumed(@Nonnull String handlerName, @Nonnull String uuid) {
    boolean[] wrapper = {true};
    String key = generateKey(handlerName, uuid);
    cache.get(key, k -> {
      wrapper[0] = false;
      return Boolean.TRUE;
    });
    return wrapper[0];
  }

  @Override
  public void remove(@Nonnull String handlerName, @Nonnull String uuid) {
    String key = generateKey(handlerName, uuid);
    cache.invalidate(key);
  }

  @Nonnull
  private String generateKey(@Nonnull String handlerName, @Nonnull String uuid) {
    return handlerName + ":" + uuid;
  }
}
