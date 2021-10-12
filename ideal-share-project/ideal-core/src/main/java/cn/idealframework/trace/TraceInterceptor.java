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
package cn.idealframework.trace;

import cn.idealframework.lang.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 宋志宗 on 2021/9/30
 */
@CommonsLog
@RequiredArgsConstructor
public class TraceInterceptor implements HandlerInterceptor {
  @Nullable
  private final TraceCollector traceCollector;

  @Override
  public void afterCompletion(@Nonnull HttpServletRequest request,
                              @Nonnull HttpServletResponse response,
                              @Nonnull Object handler, Exception ex) throws Exception {
    Object attribute = request.getAttribute(TraceConstants.REQUEST_ATTR_NAME_TRACE);
    if (attribute != null) {
      TraceContext context = (TraceContext) attribute;
      TraceInfo traceInfo = context.getTraceInfo();

      // 写入异常信息
      if (ex != null && StringUtils.isBlank(traceInfo.getException())) {
        String simpleName = ex.getClass().getSimpleName();
        String message = ex.getMessage();
        String exceptionMessage;
        if (StringUtils.isBlank(message)) {
          exceptionMessage = simpleName;
        } else {
          exceptionMessage = simpleName + ": " + message;
        }
        traceInfo.setException(exceptionMessage);
      }

      // 调用日志
      String method = request.getMethod();
      String requestURI = request.getRequestURI();
      long consuming = context.getSurvivalMillis();
      int status = response.getStatus();
      HttpStatus httpStatus = HttpStatus.resolve(status);
      if (httpStatus != null) {
        log.info(status + " " + httpStatus.getReasonPhrase() + ": " + method + " " + requestURI + " | consuming: " + consuming + "ms");
      } else {
        log.info(status + ": " + method + " " + requestURI + " | consuming: " + consuming + "ms");
      }

      // 执行耗时
      traceInfo.setConsuming(consuming);
      if (traceCollector != null) {
        try {
          traceCollector.collect(traceInfo);
        } catch (Exception e) {
          log.info("save trace log ex: " + e.getMessage());
        }
      }
      request.removeAttribute(TraceConstants.REQUEST_ATTR_NAME_TRACE);
    }
    HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
  }
}
