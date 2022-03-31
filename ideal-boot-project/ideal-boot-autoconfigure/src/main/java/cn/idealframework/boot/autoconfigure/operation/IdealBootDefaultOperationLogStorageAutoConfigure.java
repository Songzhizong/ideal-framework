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
package cn.idealframework.boot.autoconfigure.operation;

import cn.idealframework.boot.autoconfigure.database.IdealBootDatabaseProperties;
import cn.idealframework.boot.autoconfigure.operation.properties.IdealBootOperationProperties;
import cn.idealframework.boot.autoconfigure.operation.properties.StorageProperties;
import cn.idealframework.boot.autoconfigure.operation.properties.StorageType;
import cn.idealframework.boot.starter.module.operation.OperationModule;
import cn.idealframework.database.DatabaseType;
import cn.idealframework.operation.OperationLogAspect;
import cn.idealframework.operation.OperationLogStorage;
import cn.idealframework.operation.OperationLogStorageDataSourceImpl;
import cn.idealframework.operation.OperationLogStorageLogImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Nullable;
import javax.sql.DataSource;

/**
 * @author 宋志宗 on 2021/6/5
 */
@CommonsLog
@Configuration
@RequiredArgsConstructor
@ConditionalOnClass({OperationLogAspect.class, OperationModule.class})
@ConditionalOnExpression("!'${ideal.operation.storage.type}'.equalsIgnoreCase('web_client')")
public class IdealBootDefaultOperationLogStorageAutoConfigure {
  private final IdealBootOperationProperties properties;
  private final IdealBootDatabaseProperties databaseProperties;

  @Bean
  @ConditionalOnMissingBean
  public OperationLogStorage operationLogStorage(@Nullable @Autowired(required = false)
                                                     DataSource dataSource) {
    StorageProperties storage = properties.getStorage();
    StorageType storageType = storage.getType();
    if (storageType == StorageType.LOG) {
      log.info("Initializing OperationLogStorageLogImpl");
      return new OperationLogStorageLogImpl();
    }
    if (storageType == StorageType.DATABASE) {
      DatabaseType type = databaseProperties.getType();
      if (type == DatabaseType.NONE) {
        throw new IllegalArgumentException("Illegal ideal.database.type config");
      }
      if (dataSource == null) {
        throw new IllegalArgumentException("DataSource is null");
      }
      log.info("Initializing OperationLogStorageDataSourceImpl");
      return new OperationLogStorageDataSourceImpl(dataSource, type);
    }
    return null;
  }
}
