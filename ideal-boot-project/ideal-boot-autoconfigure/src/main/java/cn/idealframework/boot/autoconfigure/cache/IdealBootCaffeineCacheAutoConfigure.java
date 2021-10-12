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
import cn.idealframework.cache.MemoryCacheType;
import cn.idealframework.cache.impl.SmartMemoryCacheBuilder;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;

import javax.annotation.PostConstruct;

/**
 * @author 宋志宗 on 2021/7/11
 */
@CommonsLog
@ConditionalOnClass(CacheModule.class)
@ConditionalOnExpression("'${ideal.cache.memory-cache-type:CAFFEINE}'.equalsIgnoreCase('CAFFEINE')")
public class IdealBootCaffeineCacheAutoConfigure implements BeanPostProcessor {

  @PostConstruct
  public void initSmartMemoryCacheBuilder() {
    log.info("SmartMemoryCacheBuilder.setMemoryCacheType(MemoryCacheType.CAFFEINE)");
    SmartMemoryCacheBuilder.setMemoryCacheType(MemoryCacheType.CAFFEINE);
  }
}
