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
package cn.idealframework.boot.autoconfigure.id.snowflake;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 宋志宗 on 2021/6/5
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "ideal.snowflake")
public class IdealBootSnowflakeProperties {

  private FactoryType factoryType = FactoryType.DATABASE;

  /** 数据中心id */
  private int dataCenterId = 0;
  /** 机器id {@link IdealBootSnowflakeProperties#factoryType}为{@link FactoryType#FIXED}时生效 */
  private int machineId = 0;


  public enum FactoryType {
    /** 手动指定机器id */
    FIXED,
    /** 通过数据库注册机器id */
    DATABASE,
    /** 通过redis注册机器id */
    REDIS,
  }
}
