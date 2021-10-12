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
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.Objects;

/**
 * @author 宋志宗 on 2021/7/1
 */
public class RedisIdempotentHandler implements IdempotentHandler {
  private final String keyPrefix;
  private final Duration keyTimeout;
  private final StringRedisTemplate redisTemplate;

  public RedisIdempotentHandler(@Nonnull String keyPrefix,
                                @Nonnull Duration keyTimeout,
                                @Nonnull StringRedisTemplate redisTemplate) {
    if (!keyPrefix.endsWith(":")) {
      keyPrefix = keyPrefix + ":";
    }
    this.keyPrefix = keyPrefix;
    this.keyTimeout = keyTimeout;
    this.redisTemplate = redisTemplate;
  }

  @Override
  public boolean consumed(@Nonnull String handlerName, @Nonnull String uuid) {
    String key = generateKey(handlerName, uuid);
    Boolean absent = redisTemplate.opsForValue().setIfAbsent(key, "1", keyTimeout);
    return !Objects.requireNonNull(absent);
  }

  @Override
  public void remove(@Nonnull String handlerName, @Nonnull String uuid) {
    String key = generateKey(handlerName, uuid);
    redisTemplate.delete(key);
  }

  @Nonnull
  private String generateKey(@Nonnull String handlerName, @Nonnull String uuid) {
    return keyPrefix + handlerName + ":" + uuid;
  }
}
