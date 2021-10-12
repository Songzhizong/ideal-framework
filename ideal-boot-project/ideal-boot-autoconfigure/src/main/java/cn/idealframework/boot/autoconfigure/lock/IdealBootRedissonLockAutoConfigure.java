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
package cn.idealframework.boot.autoconfigure.lock;

import cn.idealframework.boot.starter.module.lock.LockModule;
import cn.idealframework.lock.DLockFactory;
import cn.idealframework.lock.redis.redisson.RedissonDLockFactory;
import cn.idealframework.util.Asserts;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2021/7/10
 */
@ConditionalOnClass(LockModule.class)
@ConditionalOnExpression("'${ideal.lock.dlock-type:REDISSON}'.equalsIgnoreCase('REDISSON')")
public class IdealBootRedissonLockAutoConfigure {

  @Value("${spring.application.name}")
  private String applicationName;

  @Bean
  @ConditionalOnMissingBean
  public DLockFactory dLockFactory(@Nonnull RedissonClient redissonClient) {
    Asserts.nonnull(redissonClient, "RedissonClient must be not null");
    return new RedissonDLockFactory(applicationName, redissonClient);
  }
}
