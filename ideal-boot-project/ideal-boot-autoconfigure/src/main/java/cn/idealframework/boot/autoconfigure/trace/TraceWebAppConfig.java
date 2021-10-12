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

import cn.idealframework.boot.starter.module.operation.TraceModule;
import cn.idealframework.trace.TraceFilter;
import cn.idealframework.trace.TraceInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Nonnull;
import javax.servlet.Filter;

/**
 * @author 宋志宗 on 2021/9/30
 */
@RequiredArgsConstructor
@ConditionalOnClass({TraceFilter.class, TraceModule.class, Filter.class})
public class TraceWebAppConfig implements WebMvcConfigurer {
  private final TraceInterceptor traceInterceptor;

  @Override
  public void addInterceptors(@Nonnull InterceptorRegistry registry) {
    registry.addInterceptor(traceInterceptor).addPathPatterns("/**");
  }

}
