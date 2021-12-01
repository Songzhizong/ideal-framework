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

import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 宋志宗 on 2021/6/7
 */
@CommonsLog
@RequiredArgsConstructor
public class FixedSnowFlakeFactory implements SnowflakeFactory {
  private final Map<String, SnowFlake> idGeneratorMap = new ConcurrentHashMap<>();

  private final long dataCenterId;
  private final long machineId;

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
    return idGeneratorMap.computeIfAbsent(biz, k -> new SnowFlake(dataCenterId, machineId));
  }

  @Override
  public void release() {

  }
}
