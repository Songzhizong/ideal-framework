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

import ch.qos.logback.classic.pattern.ClassicConverter;
import cn.idealframework.trace.LogCollector;
import cn.idealframework.trace.TraceableMessageConverter;
import cn.idealframework.trace.impl.LogTraceCollector;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2021/6/10
 */
@CommonsLog
@Configuration
@ConditionalOnClass({ClassicConverter.class})
public class IdealBootTraceLogbackInitializingBean implements InitializingBean {
  private final LogCollector logCollector;

  public IdealBootTraceLogbackInitializingBean(@Nullable @Autowired(required = false)
                                                   LogCollector logCollector) {
    this.logCollector = logCollector;
  }

  @Override
  public void afterPropertiesSet() {
    if (!(logCollector instanceof LogTraceCollector)) {
      TraceableMessageConverter.setLogCollector(logCollector);
    }
  }
}
