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

import cn.idealframework.lang.ArrayUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author 宋志宗 on 2021/6/4
 */
@SuppressWarnings("SpellCheckingInspection")
@Aspect
@CommonsLog
@RequiredArgsConstructor
public class TraceInfoAspect {
  private static final ConcurrentMap<String, Boolean> packageMap = new ConcurrentHashMap<>();
  private final boolean enabled;
  /** 接口调用日志扫描包,默认全部接口 */
  @Nullable
  private final Set<String> basePackages;
  /** 需要排除的包路径 */
  @Nullable
  private final Set<String> excludePackages;

  @Around("@within(annotation)")
  public Object RestControllerProcess(@Nonnull ProceedingJoinPoint joinPoint,
                                      RestController annotation) throws Throwable {
    boolean initialized = TraceContextHolder.isInitialized();
    if (enabled && initialized) {
      return controllerAround(joinPoint);
    } else {
      return joinPoint.proceed();
    }
  }

  @Around("@within(annotation)")
  public Object controllerProcess(@Nonnull ProceedingJoinPoint joinPoint,
                                  Controller annotation) throws Throwable {
    boolean initialized = TraceContextHolder.isInitialized();
    if (enabled && initialized) {
      return controllerAround(joinPoint);
    } else {
      return joinPoint.proceed();
    }
  }

  @Nullable
  private Object controllerAround(@Nonnull ProceedingJoinPoint joinPoint) throws Throwable {
    Class<?> targetClass = joinPoint.getTarget().getClass();
    String packageName = targetClass.getPackage().getName();
    if (!packageJudge(packageName)) {
      return joinPoint.proceed();
    }
    Optional<TraceContext> optional = TraceContextHolder.current();
    if (!optional.isPresent()) {
      return joinPoint.proceed();
    }
    TraceContext context = optional.get();
    TraceInfo traceInfo = context.getTraceInfo();
    Object proceed = null;
    try {
      proceed = joinPoint.proceed();
      return proceed;
    } finally {
      try {
        CodeSignature signature = (CodeSignature) joinPoint.getSignature();
        String methodName = signature.getName();
        //noinspection rawtypes
        Class[] parameterTypes = signature.getParameterTypes();
        Method method = targetClass.getMethod(methodName, parameterTypes);
        TracePoint annotation = method.getAnnotation(TracePoint.class);
        Map<String, Object> args = traceInfo.getRequest();
        if ((args == null || args.isEmpty())
          && (annotation == null || annotation.recordReq())) {
          traceInfo.setRequest(getReqArgs(joinPoint, annotation));
        }
        if (proceed != null) {
          if (annotation != null && annotation.recordResp()) {
            if (!(proceed instanceof DeferredResult)) {
              traceInfo.setResponse(proceed);
            }
          }
        }
      } catch (Exception e) {
        log.warn("Record trace log exception: ", e);
      }
    }
  }

  @Nonnull
  private Map<String, Object> getReqArgs(@Nonnull ProceedingJoinPoint joinPoint,
                                         @Nullable TracePoint annotation) {
    int[] ints = null;
    if (annotation == null) {
      ints = ArrayUtils.EMPTY_INT_ARRAY;
    }
    if (annotation != null && !annotation.excludeAllArgs()) {
      ints = annotation.exclusionArgs();
    }
    if (ints != null) {
      Object[] args = joinPoint.getArgs();
      Map<String, Object> argMap = new LinkedHashMap<>();
      for (int i = 0; i < args.length; i++) {
        if (!ArrayUtils.contains(ints, i)) {
          Object arg = args[i];
          if (arg != null) {
            if (arg instanceof ServletRequest) {
              continue;
            }
            if (arg instanceof ServletResponse) {
              continue;
            }
            if (arg instanceof HttpSession) {
              continue;
            }
            if (arg instanceof MultipartFile) {
              continue;
            }
            if (arg instanceof ModelAndView) {
              continue;
            }
            if (arg instanceof byte[]) {
              continue;
            }
            if (arg instanceof Byte[]) {
              continue;
            }
          }
          argMap.put(i + "", arg);
        }
      }
      return argMap;
    }
    return Collections.emptyMap();
  }

  private boolean packageJudge(@Nonnull String packageName) {
    return packageMap.computeIfAbsent(packageName, k -> {
      if (excludePackages != null) {
        for (String excludePackage : excludePackages) {
          if (packageName.startsWith(excludePackage)) {
            return false;
          }
        }
      }
      if (basePackages != null) {
        for (String basePackage : basePackages) {
          if (packageName.startsWith(basePackage)) {
            return true;
          }
        }
      }
      return true;
    });
  }
}
