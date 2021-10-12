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

import cn.idealframework.boot.starter.module.snowflake.SnowflakeModule;
import cn.idealframework.id.IDGenerator;
import cn.idealframework.id.IDGeneratorFactory;
import cn.idealframework.id.JpaIDGenerator;
import cn.idealframework.id.snowflake.SnowFlake;
import lombok.extern.apachecommons.CommonsLog;
import org.hibernate.id.IdentityGenerator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2021/6/9
 */
@CommonsLog
@Configuration
@ConditionalOnClass({SnowFlake.class, SnowflakeModule.class, IdentityGenerator.class})
public class IdealBootSnowflakeJpaIDGeneratorInitializing implements InitializingBean {
  @Nullable
  private final IDGeneratorFactory idGeneratorFactory;

  public IdealBootSnowflakeJpaIDGeneratorInitializing(@Nullable @Autowired(required = false)
                                                          IDGeneratorFactory idGeneratorFactory) {
    this.idGeneratorFactory = idGeneratorFactory;
  }

  @Override
  public void afterPropertiesSet() {
    if (idGeneratorFactory == null) {
      log.warn("IDGeneratorFactory is null");
      return;
    }
    IDGenerator idGenerator = JpaIDGenerator.getIdGenerator();
    if (idGenerator == null) {
      IDGenerator jpaIDGenerator = idGeneratorFactory.getGenerator("jpa");
      JpaIDGenerator.setIdGenerator(jpaIDGenerator);
    }
  }
}
