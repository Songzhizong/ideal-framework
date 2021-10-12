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
package cn.idealframework.boot.autoconfigure.trace;

import cn.idealframework.boot.autoconfigure.trace.properties.CollectorProperties;
import cn.idealframework.boot.autoconfigure.trace.properties.IdealBootTraceProperties;
import cn.idealframework.boot.starter.module.operation.TraceModule;
import cn.idealframework.trace.TraceCollector;
import cn.idealframework.trace.impl.LogTraceCollector;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2021/6/10
 */
@CommonsLog
@Configuration
@RequiredArgsConstructor
@ConditionalOnClass(TraceModule.class)
@ConditionalOnExpression("!'${ideal.trace.collector.collect-type}'.equalsIgnoreCase('WEB_CLIENT')")
public class IdealBootTraceDefaultCollectorAutoConfigure {

  @Bean
  @ConditionalOnMissingBean
  public TraceCollector traceCollector(@Nonnull IdealBootTraceProperties properties) {
    CollectorProperties collector = properties.getCollector();
    CollectorProperties.CollectType collectType = collector.getCollectType();
    if (collectType == CollectorProperties.CollectType.LOG) {
      boolean prettyOutput = collector.getLog().isPrettyOutput();
      return new LogTraceCollector(prettyOutput);
    }
    return null;
  }
}
