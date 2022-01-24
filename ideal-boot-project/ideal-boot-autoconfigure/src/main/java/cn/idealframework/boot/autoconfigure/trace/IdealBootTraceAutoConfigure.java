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

import cn.idealframework.boot.autoconfigure.application.IdealBootApplicationProperties;
import cn.idealframework.boot.autoconfigure.trace.properties.ArgsRecordProperties;
import cn.idealframework.boot.autoconfigure.trace.properties.IdealBootTraceProperties;
import cn.idealframework.boot.starter.module.operation.TraceModule;
import cn.idealframework.id.IDGenerator;
import cn.idealframework.id.IDGeneratorFactory;
import cn.idealframework.lang.StringUtils;
import cn.idealframework.trace.*;
import cn.idealframework.trace.impl.IDGeneratorTraceIdGenerator;
import cn.idealframework.trace.impl.UUIDTraceIdGenerator;
import cn.idealframework.util.IpUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Nullable;
import javax.servlet.Filter;
import java.util.Set;

/**
 * @author 宋志宗 on 2021/6/5
 */
@CommonsLog
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(IdealBootTraceProperties.class)
@ConditionalOnClass({TraceFilter.class, TraceModule.class, Filter.class})
//@ConditionalOnProperty(prefix = IDEAL_BOOT_TRACE_PREFIX, name = "enabled", havingValue = "true")
public class IdealBootTraceAutoConfigure {
  private final IdealBootTraceProperties properties;
  private final IdealBootApplicationProperties applicationProperties;

  @Value("${server.port:8080}")
  private int port;

  @Value("${spring.application.name:UNKNOWN}")
  private String applicationName;

  @Bean
  @ConditionalOnMissingBean
  public TraceIdGenerator traceIdGenerator(
    @Nullable @Autowired(required = false)
      IDGeneratorFactory idGeneratorFactory) {
    if (idGeneratorFactory != null) {
      IDGenerator generator = idGeneratorFactory.getGenerator("traceIdGenerator");
      return new IDGeneratorTraceIdGenerator(generator);
    }
    return UUIDTraceIdGenerator.getInstance();
  }

  @Bean
  public TraceFilter traceFilter(
    @Nullable @Autowired(required = false)
      TraceCollector traceCollector,
    @Nullable @Autowired(required = false)
      PreFilterHandler preFilterHandler) {
    IpUtils.getLocalAddress();
    if (traceCollector == null) {
      log.warn("TraceCollector is null");
    }
    String applicationName = applicationProperties.getName();
    if (StringUtils.isBlank(applicationName)) {
      applicationName = this.applicationName;
    }
    Set<String> expirePatterns = properties.getExcludePatterns();
    return new TraceFilter(preFilterHandler, port, applicationName, expirePatterns);
  }

  @Bean
  public TraceInterceptor traceInterceptor(@Nullable @Autowired(required = false)
                                             TraceCollector traceCollector) {
    return new TraceInterceptor(traceCollector);
  }

  @Bean
  public TraceInfoAspect traceLogAspect() {
    log.info("Initializing TraceLogAspect");
    ArgsRecordProperties argsRecord = properties.getArgsRecord();
    boolean enabled = argsRecord.isEnabled();
    Set<String> basePackages = argsRecord.getBasePackages();
    Set<String> excludePackages = argsRecord.getExcludePackages();
    return new TraceInfoAspect(enabled, basePackages, excludePackages);
  }
}
