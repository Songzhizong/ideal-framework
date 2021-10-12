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

import cn.idealframework.boot.autoconfigure.database.IdealBootDatabaseProperties;
import cn.idealframework.boot.starter.module.snowflake.SnowflakeModule;
import cn.idealframework.id.IDGeneratorFactory;
import cn.idealframework.id.snowflake.DatabaseSnowFlakeFactory;
import cn.idealframework.id.snowflake.FixedSnowFlakeFactory;
import cn.idealframework.id.snowflake.SnowFlake;
import cn.idealframework.lang.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Nullable;
import javax.sql.DataSource;

/**
 * @author 宋志宗 on 2021/6/7
 */
@CommonsLog
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(IdealBootSnowflakeProperties.class)
@ConditionalOnClass({SnowFlake.class, SnowflakeModule.class})
@ConditionalOnExpression("!'${ideal.snowflake.factory-type}'.equalsIgnoreCase('REDIS')")
public class IdealBootSnowflakeIDGeneratorFactoryAutoConfigure {
  private final IdealBootSnowflakeProperties properties;
  private final IdealBootDatabaseProperties databaseProperties;

  @Value("${spring.application.name:}")
  private String applicationName;

  @Bean
  @ConditionalOnMissingBean
  public IDGeneratorFactory idGeneratorFactory(@Nullable @Autowired(required = false)
                                                   DataSource dataSource) {
    IdealBootSnowflakeProperties.FactoryType factoryType = properties.getFactoryType();
    int dataCenterId = properties.getDataCenterId();
    if (factoryType == IdealBootSnowflakeProperties.FactoryType.FIXED) {
      int machineId = properties.getMachineId();
      return new FixedSnowFlakeFactory(dataCenterId, machineId);
    }
    if (factoryType == IdealBootSnowflakeProperties.FactoryType.DATABASE) {
      if (StringUtils.isBlank(applicationName)) {
        throw new IllegalArgumentException("spring.application.name is blank");
      }
      if (dataSource == null) {
        throw new IllegalArgumentException("DataSource is null");
      }
      return new DatabaseSnowFlakeFactory(dataCenterId, applicationName, dataSource);
    }
    return null;
  }
}
