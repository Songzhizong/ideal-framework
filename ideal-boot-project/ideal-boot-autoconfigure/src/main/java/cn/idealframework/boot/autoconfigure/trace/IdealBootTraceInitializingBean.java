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

import cn.idealframework.trace.TraceContextHolder;
import cn.idealframework.trace.TraceIdGenerator;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2021/6/6
 */
@CommonsLog
@Configuration
public class IdealBootTraceInitializingBean implements InitializingBean {
  private final TraceIdGenerator traceIdGenerator;

  public IdealBootTraceInitializingBean(@Nullable @Autowired(required = false)
                                            TraceIdGenerator traceIdGenerator) {
    this.traceIdGenerator = traceIdGenerator;
  }

  @Override
  public void afterPropertiesSet() {
    if (traceIdGenerator != null) {
      String className = traceIdGenerator.getClass().getName();
      log.info("Using TraceIdGenerator impl: " + className);
      TraceContextHolder.setTraceIdGenerator(traceIdGenerator);
    }
  }
}
