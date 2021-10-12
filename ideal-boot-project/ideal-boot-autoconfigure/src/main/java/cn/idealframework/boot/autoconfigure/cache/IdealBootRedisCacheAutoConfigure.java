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
package cn.idealframework.boot.autoconfigure.cache;

import cn.idealframework.boot.starter.module.cache.CacheModule;
import cn.idealframework.cache.DistributedCacheType;
import cn.idealframework.cache.impl.RedisCache;
import cn.idealframework.cache.impl.RedisHashCache;
import cn.idealframework.cache.impl.SmartDistributedCacheBuilder;
import cn.idealframework.cache.impl.SmartDistributedHashCacheBuilder;
import cn.idealframework.lock.DLockFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.PostConstruct;

/**
 * @author 宋志宗 on 2021/7/11
 */
@SuppressWarnings("SpringJavaAutowiredMembersInspection")
@CommonsLog
@RequiredArgsConstructor
@ConditionalOnClass(CacheModule.class)
@ConditionalOnExpression("'${ideal.cache.distributed-cache-type:REDIS}'.equalsIgnoreCase('REDIS')")
public class IdealBootRedisCacheAutoConfigure implements BeanPostProcessor, SmartInitializingSingleton {
  private final IdealBootCacheProperties properties;
  @Autowired
  private DLockFactory lockFactory;
  @Autowired(required = false)
  private StringRedisTemplate redisTemplate;

  @PostConstruct
  public void initSmartDistributedCacheBuilder() {
    log.info("SmartDistributedCacheBuilder.setDistributedCacheType(DistributedCacheType.REDIS)");
    SmartDistributedCacheBuilder.setDistributedCacheType(DistributedCacheType.REDIS);
    SmartDistributedHashCacheBuilder.setDistributedCacheType(DistributedCacheType.REDIS);
    String prefix = properties.getRedis().getPrefix();
    if (!prefix.endsWith(":")) {
      prefix = prefix + ":";
    }
    RedisCache.setGlobalPrefix(prefix);
    RedisHashCache.setGlobalPrefix(prefix);
  }


  @Override
  public void afterSingletonsInstantiated() {
    RedisCache.setRedisTemplate(redisTemplate);
    RedisCache.setLockFactory(lockFactory);
    RedisHashCache.setRedisTemplate(redisTemplate);
    RedisHashCache.setLockFactory(lockFactory);
    log.info("Init RedisCache...");
  }
}
