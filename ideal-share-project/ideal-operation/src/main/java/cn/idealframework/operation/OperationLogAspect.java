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
package cn.idealframework.operation;

import cn.idealframework.date.DateTimes;
import cn.idealframework.extensions.ServletUtils;
import cn.idealframework.extensions.spring.SpelUtils;
import cn.idealframework.lang.StringUtils;
import cn.idealframework.trace.TraceContextHolder;
import lombok.extern.apachecommons.CommonsLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @author 宋志宗 on 2021/6/4
 */
@SuppressWarnings("DuplicatedCode")
@Aspect
@CommonsLog
public class OperationLogAspect {
  private final boolean enabled;
  @Nonnull
  private final OperatorContextHolder contextHolder;
  @Nonnull
  private final OperationLogStorage operationLogStorage;

  public OperationLogAspect(boolean enabled,
                            @Nonnull OperatorContextHolder contextHolder,
                            @Nonnull OperationLogStorage operationLogStorage) {
    this.enabled = enabled;
    this.contextHolder = contextHolder;
    this.operationLogStorage = operationLogStorage;
    if (!enabled) {
      log.info("Operation log is disabled");
    }
  }


  @Around("@annotation(annotation)")
  public Object around(@Nonnull ProceedingJoinPoint joinPoint, OperationLog annotation) throws Throwable {
    if (!enabled) {
      return joinPoint.proceed();
    }
    OperatorContext context = contextHolder.current().orElse(null);
    if (context == null) {
      return joinPoint.proceed();
    }
    OperationLogInfo operationLogInfo = OperationLogs.init();
    try {
      TraceContextHolder.current()
        .ifPresent(traceContext -> operationLogInfo.setTraceId(traceContext.getTraceId()));
      String system = annotation.system();
      if (StringUtils.isNotBlank(system)) {
        operationLogInfo.setSystem(system);
      } else {
        operationLogInfo.setSystem(context.getSystem());
      }
      operationLogInfo.setOperation(annotation.operation());
      operationLogInfo.setOperationTime(DateTimes.now());
      // requestURI clientIp
      try {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
          log.warn("RequestContextHolder.getRequestAttributes() result is null");
        } else {
          HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
          // requestURI
          String requestURI = request.getRequestURI();
          operationLogInfo.setUri(requestURI);
          // clientIp
          String originalIp = ServletUtils.getOriginalIp(request);
          operationLogInfo.setOriginalIp(originalIp);
          // UA
          String userAgent = request.getHeader(HttpHeaders.USER_AGENT);
          operationLogInfo.setUserAgent(userAgent);
        }
      } catch (Exception e) {
        log.warn("get request info ex: ", e);
      }
      operationLogInfo.setTenantId(context.getTenantId());
      operationLogInfo.setUserId(context.getUserId());
      operationLogInfo.setUsername(context.getUsername());
      operationLogInfo.setSuccess(true);
      operationLogInfo.setMessage("success");
      try {
        return joinPoint.proceed();
      } catch (Throwable throwable) {
        operationLogInfo.setSuccess(false);
        operationLogInfo.setMessage(throwable.getMessage());
        throw throwable;
      }
    } finally {
      try {
        String description = operationLogInfo.getDetails();
        String desc = annotation.desc();
        if (StringUtils.isBlank(description) && StringUtils.isNotBlank(desc)) {
          MethodSignature signature = (MethodSignature) joinPoint.getSignature();
          Method method = signature.getMethod();
          Object[] args = joinPoint.getArgs();
          String parseDesc = SpelUtils.parseSpel(desc, method, args);
          if (parseDesc != null) {
            operationLogInfo.setDetails(parseDesc);
          }
        }
      } catch (Exception e) {
        log.warn("Parse operation desc ex: ", e);
      }
      try {
        operationLogStorage.save(operationLogInfo);
      } catch (Exception e) {
        log.warn("Save operation log ex: ", e);
      }
      OperationLogs.release();
    }
  }
}
