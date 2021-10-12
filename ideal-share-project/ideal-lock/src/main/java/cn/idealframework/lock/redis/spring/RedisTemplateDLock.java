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
package cn.idealframework.lock.redis.spring;

import cn.idealframework.lock.DLock;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author 宋志宗 on 2021/9/2
 */
public class RedisTemplateDLock implements DLock {
  private static final byte[] SCRIPT
      = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end"
    .getBytes(StandardCharsets.UTF_8);

  private final String lockName;
  private final String lockKey;
  private final long leaseTimeMillis;
  private final StringRedisTemplate redisTemplate;
  private final String lockValue = UUID.randomUUID().toString();
  @Nullable
  private volatile Long threadId = null;

  public RedisTemplateDLock(@Nonnull String lockName,
                            @Nonnull String lockKey,
                            long leaseTimeMillis,
                            @Nonnull StringRedisTemplate redisTemplate) {
    this.lockName = lockName;
    this.lockKey = lockKey;
    this.leaseTimeMillis = leaseTimeMillis;
    this.redisTemplate = redisTemplate;
  }

  @Nonnull
  @Override
  public String getName() {
    return lockName;
  }

  @Override
  public boolean tryLock(@Nonnull Duration waitTime) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void forceUnlock() {
    redisTemplate.delete(lockKey);
    this.threadId = null;
  }

  @Override
  public void lock() {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean tryLock() {
    long threadId = Thread.currentThread().getId();
    //noinspection ConstantConditions
    if (this.threadId != null && this.threadId == threadId) {
      return true;
    }
    Boolean absent = redisTemplate.opsForValue()
        .setIfAbsent(lockKey, lockValue, leaseTimeMillis, TimeUnit.MILLISECONDS);
    boolean lockSuccess = absent != null && absent;
    this.threadId = threadId;
    return lockSuccess;
  }

  @Override
  public void unlock() {
    //noinspection ConstantConditions
    if (this.threadId == null || this.threadId != Thread.currentThread().getId()) {
      return;
    }
    RedisConnectionFactory factory = redisTemplate.getConnectionFactory();
    RedisConnection connection = Objects.requireNonNull(factory).getConnection();
    Long eval = connection.scriptingCommands().<Long>eval(
        SCRIPT, ReturnType.INTEGER, 1,
        lockKey.getBytes(Charset.defaultCharset()),
        lockValue.getBytes(Charset.defaultCharset())
    );
    if (eval != null && eval > 0) {
      this.threadId = null;
      return;
    }
    Boolean hasKey = redisTemplate.hasKey(lockKey);
    if (hasKey != null && !hasKey) {
      this.threadId = null;
    }
  }
}
