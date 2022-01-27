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
package cn.idealframework.id.snowflake;

import cn.idealframework.util.Asserts;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * 基于redis的SnowFlake生成器工厂
 *
 * @author 宋志宗 on 2020/10/21
 */
@CommonsLog
public class SpringRedisSnowFlakeFactory implements SnowflakeFactory, SnowflakeMachineIdHolder {
  private final String value = UUID.randomUUID().toString();
  private final ConcurrentMap<String, SnowFlake> generatorMap = new ConcurrentHashMap<>();
  private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
  private final String prefix;
  private final Duration expire;
  @Nonnull
  private final StringRedisTemplate redisTemplate;

  private final long dataCenterId;
  private final String applicationName;
  private volatile long machineId;

  /**
   * @param applicationName 应用名称
   * @param redisTemplate   {@link StringRedisTemplate}
   */
  public SpringRedisSnowFlakeFactory(
    @Nonnull String applicationName,
    @Nonnull StringRedisTemplate redisTemplate) {
    this(0, applicationName, redisTemplate);
  }

  /**
   * @param dataCenterId    数据中心id
   * @param applicationName 应用名称
   * @param redisTemplate   {@link StringRedisTemplate}
   */
  public SpringRedisSnowFlakeFactory(
    long dataCenterId,
    @Nonnull String applicationName,
    @Nonnull StringRedisTemplate redisTemplate) {
    this(dataCenterId, 600, 30,
      applicationName, redisTemplate);
  }

  /**
   * @param dataCenterId           数据中心id
   * @param expireSeconds          机器码过期时间
   * @param renewalIntervalSeconds 机器码续期间隔,需要小于过期时间
   * @param applicationName        应用名称
   * @param redisTemplate          {@link StringRedisTemplate}
   */
  public SpringRedisSnowFlakeFactory(
    long dataCenterId,
    long expireSeconds,
    long renewalIntervalSeconds,
    @Nonnull String applicationName,
    @Nonnull StringRedisTemplate redisTemplate) {
    int maxDataCenterNum = SnowFlake.MAX_DATA_CENTER_NUM;
    if (dataCenterId < 0 || dataCenterId > maxDataCenterNum) {
      log.warn("dataCenterId must >=0 and <=" + maxDataCenterNum);
      dataCenterId = 0;
    }
    if (renewalIntervalSeconds >= expireSeconds) {
      expireSeconds = renewalIntervalSeconds << 1;
    }
    this.dataCenterId = dataCenterId;
    this.prefix = "ideal:register:snowflake:machineId:" + applicationName + ":";
    this.expire = Duration.ofSeconds(expireSeconds);
    this.redisTemplate = redisTemplate;
    this.applicationName = applicationName;
    this.machineId = calculateMachineId();
    Asserts.assertTrue(this.machineId > -1, "计算机器码失败");
    executorService.scheduleAtFixedRate(() -> {
        try {
          this.heartbeat();
        } catch (Exception e) {
          log.warn("", e);
        }
      },
      renewalIntervalSeconds, renewalIntervalSeconds, TimeUnit.SECONDS);
    Runtime.getRuntime().addShutdownHook(new Thread(this::release));
    log.info("SnowFlake dataCenterId = " + dataCenterId + ", machineId = " + machineId);
  }

  private int calculateMachineId() {
    ValueOperations<String, String> operations = this.redisTemplate.opsForValue();
    int machineId = -1;
    while (true) {
      ++machineId;
      Boolean success = operations
        .setIfAbsent(prefix + machineId, value, expire);
      if (success != null && success) {
        log.info("SnowFlake register success: applicationName = " + this.applicationName + ", machineId = " + machineId);
        break;
      }
      if (machineId >= SnowFlake.MAX_MACHINE_NUM) {
        log.error("SnowFlake machineId 计算失败,已达上限: " + SnowFlake.MAX_MACHINE_NUM + " applicationName = " + this.applicationName);
        return -1;
      }
    }
    return machineId;
  }

  private void heartbeat() {
    String key = prefix + this.machineId;
    String value = redisTemplate.opsForValue().get(key);
    if (this.value.equals(value)) {
      redisTemplate.expire(key, expire);
      return;
    }
    int machineId = calculateMachineId();
    if (machineId > -1) {
      this.machineId = machineId;
    }
  }

  @Override
  public long dataCenterId() {
    return dataCenterId;
  }

  @Override
  public long machineId() {
    return machineId;
  }

  @Nonnull
  @Override
  public SnowFlake getGenerator(@Nonnull String biz) {
    return generatorMap.computeIfAbsent(biz, k -> new SnowFlake(dataCenterId, this));
  }

  @Override
  public void release() {
    // 释放机器码
    executorService.shutdown();
    redisTemplate.delete(prefix + this.machineId);
  }

  @Override
  public long getCurrentMachineId() {
    return machineId;
  }
}
