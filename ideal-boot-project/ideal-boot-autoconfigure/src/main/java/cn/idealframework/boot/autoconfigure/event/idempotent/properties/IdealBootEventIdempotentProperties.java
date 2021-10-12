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
package cn.idealframework.boot.autoconfigure.event.idempotent.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author 宋志宗 on 2021/7/1
 */
@Getter
@Setter
public class IdealBootEventIdempotentProperties {
  /** 处理器类型 */
  private IdempotentType type = IdempotentType.REDIS;

  /** redis幂等处理器配置 */
  @NestedConfigurationProperty
  private IdealBootEventRedisIdempotentProperties redis = new IdealBootEventRedisIdempotentProperties();

  /** 内存幂等处理器配置 */
  @NestedConfigurationProperty
  private IdealBootEventCaffeineIdempotentProperties memory = new IdealBootEventCaffeineIdempotentProperties();

  public enum IdempotentType {
    /** 不启用幂等 */
    NONE,
    /** 基于本地内存的幂等处理器 */
    MEMORY,
    /** 基于redis的幂等处理器 */
    REDIS,
  }
}
