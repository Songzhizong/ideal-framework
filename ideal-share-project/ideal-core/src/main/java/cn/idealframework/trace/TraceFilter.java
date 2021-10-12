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

import cn.idealframework.extensions.ServletUtils;
import cn.idealframework.lang.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.AntPathMatcher;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author 宋志宗 on 2021/6/2
 */
@CommonsLog
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TraceFilter implements Filter {
  private static final AntPathMatcher matcher = new AntPathMatcher();
  @Nullable
  private final PreFilterHandler preFilterHandler;
  /** 服务端口号 */
  private final int serverPort;
  /** 应用名称 */
  private final String application;
  private final Set<String> excludePatterns;

  @Override
  public void doFilter(@Nonnull ServletRequest request,
                       @Nonnull ServletResponse response,
                       @Nonnull FilterChain chain) throws IOException, ServletException {
    HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    String requestURI = httpServletRequest.getRequestURI();
    if (isExpired(requestURI)) {
      chain.doFilter(request, response);
      return;
    }
    String traceId = httpServletRequest.getHeader(TraceConstants.HTTP_HEADER_TRACE_ID);
    if (StringUtils.isBlank(traceId)) {
      traceId = null;
    }
    String spanId = httpServletRequest.getHeader(TraceConstants.HTTP_HEADER_SPAN_ID);
    if (StringUtils.isBlank(spanId)) {
      spanId = null;
    }
    TraceContext context = TraceContextHolder.init("http", traceId, spanId);
    TraceInfo traceInfo = context.getTraceInfo();
    request.setAttribute(TraceConstants.REQUEST_ATTR_NAME_TRACE, context);
    try {
      traceInfo.setApplication(application);
      traceInfo.setExecuteTime(LocalDateTime.now());
      traceInfo.setTarget(requestURI);
      traceInfo.setClientIp(ServletUtils.getOriginalIp(httpServletRequest));
      traceInfo.setServerPort(serverPort);
      if (preFilterHandler != null) {
        try {
          preFilterHandler.handle(httpServletRequest, context);
        } catch (Exception e) {
          log.info("preFilter ex: " + e.getMessage());
        }
      }
      chain.doFilter(request, response);
    } catch (Exception exception) {
      if (StringUtils.isBlank(traceInfo.getException())) {
        String simpleName = exception.getClass().getSimpleName();
        String message = exception.getMessage();
        String exceptionMessage;
        if (StringUtils.isBlank(message)) {
          exceptionMessage = simpleName;
        } else {
          exceptionMessage = simpleName + ": " + message;
        }
        traceInfo.setException(exceptionMessage);
      }
      throw exception;
    } finally {
      TraceContextHolder.release();
    }
  }

  private boolean isExpired(@Nonnull String requestURI) {
    if (excludePatterns == null || excludePatterns.isEmpty()) {
      return false;
    }
    for (String expire : excludePatterns) {
      if (matcher.match(expire, requestURI)) {
        return true;
      }
    }
    return false;
  }
}
