///*
// * Copyright 2021 cn.idealframework
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      https://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package cn.idealframework.operation;
//
//import cn.idealframework.date.DateTimes;
//import cn.idealframework.extensions.ServletUtils;
//import cn.idealframework.extensions.spring.SpelUtils;
//import cn.idealframework.lang.StringUtils;
//import cn.idealframework.trace.TraceContextHolder;
//import lombok.extern.apachecommons.CommonsLog;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.http.HttpHeaders;
//import org.springframework.web.context.request.RequestAttributes;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.annotation.Nonnull;
//import javax.servlet.http.HttpServletRequest;
//import java.lang.reflect.Method;
//
///**
// * @author 宋志宗 on 2021/6/4
// */
//@Aspect
//@CommonsLog
//public class OperationLogAspect {
//  private final boolean enabled;
//  @Nonnull
//  private final OperatorContext operatorHolder;
//  @Nonnull
//  private final OperationLogStorage operationLogStorage;
//
//  public OperationLogAspect(boolean enabled,
//                            @Nonnull OperatorContext operatorHolder,
//                            @Nonnull OperationLogStorage operationLogStorage) {
//    this.enabled = enabled;
//    this.operatorHolder = operatorHolder;
//    this.operationLogStorage = operationLogStorage;
//    if (!enabled) {
//      log.info("Operation log is disabled");
//    }
//  }
//
//
//  @Around("@annotation(annotation)")
//  public Object around(@Nonnull ProceedingJoinPoint joinPoint, Operation annotation) throws Throwable {
//    if (!enabled) {
//      return joinPoint.proceed();
//    }
//    OperationLog operationLog = OperationLogHolder.get();
//    try {
//      TraceContextHolder.current()
//        .ifPresent(traceContext -> operationLog.setTraceId(traceContext.getTraceId()));
//      String system = annotation.system();
//      if (StringUtils.isNotBlank(system)) {
//        operationLog.setSystem(system);
//      } else {
//        operationLog.setSystem(operatorHolder.getSystem());
//      }
//      operationLog.setOperation(annotation.operation());
//      operationLog.setOperationTime(DateTimes.now());
//      // requestURI clientIp
//      try {
//        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
//        if (requestAttributes == null) {
//          log.warn("RequestContextHolder.getRequestAttributes() result is null");
//        } else {
//          HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
//          // requestURI
//          String requestURI = request.getRequestURI();
//          operationLog.setUri(requestURI);
//          // clientIp
//          String originalIp = ServletUtils.getOriginalIp(request);
//          operationLog.setClientIp(originalIp);
//          // UA
//          String userAgent = request.getHeader(HttpHeaders.USER_AGENT);
//          operationLog.setUserAgent(userAgent);
//        }
//      } catch (Exception e) {
//        log.warn("get request info ex: ", e);
//      }
//      operationLog.setTenantId(operatorHolder.getTenantId());
//      operationLog.setUserId(operatorHolder.getUserId());
//      operationLog.setUsername(operatorHolder.getUserName());
//      operationLog.setSuccess(true);
//      operationLog.setMessage("success");
//      try {
//        return joinPoint.proceed();
//      } catch (Throwable throwable) {
//        operationLog.setSuccess(false);
//        operationLog.setMessage(throwable.getMessage());
//        throw throwable;
//      }
//    } finally {
//      try {
//        String description = operationLog.getDescription();
//        String desc = annotation.desc();
//        if (StringUtils.isBlank(description) && StringUtils.isNotBlank(desc)) {
//          MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//          Method method = signature.getMethod();
//          Object[] args = joinPoint.getArgs();
//          String parseDesc = SpelUtils.parseSpel(desc, method, args);
//          if (parseDesc != null) {
//            operationLog.setDescription(parseDesc);
//          }
//        }
//      } catch (Exception e) {
//        log.warn("Parse operation desc ex: ", e);
//      }
//      try {
//        operationLogStorage.save(operationLog);
//      } catch (Exception e) {
//        log.warn("Save operation log ex: ", e);
//      }
//      OperationLogHolder.release();
//    }
//  }
//}
