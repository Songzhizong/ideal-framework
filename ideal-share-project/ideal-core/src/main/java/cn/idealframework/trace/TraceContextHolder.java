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
import cn.idealframework.trace.impl.UUIDTraceIdGenerator;
import lombok.Setter;
import lombok.extern.apachecommons.CommonsLog;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * @author 宋志宗 on 2021/5/31
 */
@CommonsLog
public final class TraceContextHolder {
  private static final ThreadLocal<TraceContext> TRACE_CONTEXT_THREAD_LOCAL = new ThreadLocal<>();
  @Setter
  @Nonnull
  private static TraceIdGenerator traceIdGenerator = UUIDTraceIdGenerator.getInstance();


  @SuppressWarnings("UnusedReturnValue")
  public static TraceContext init(@Nonnull TraceContext context) {
    TRACE_CONTEXT_THREAD_LOCAL.set(context);
    return context;
  }

  @Nonnull
  public static TraceContext init(@Nonnull String mode,
                                  @Nullable String traceId,
                                  @Nullable String spanId) {
    if (StringUtils.isBlank(traceId)) {
      traceId = traceIdGenerator.generateTraceId();
      spanId = "1";
    }
    if (StringUtils.isBlank(spanId)) {
      spanId = "1";
    }
    TraceContext context = new TraceContext(mode, traceId, spanId);
    TRACE_CONTEXT_THREAD_LOCAL.set(context);
    return context;
  }

  public static boolean isInitialized() {
    return TRACE_CONTEXT_THREAD_LOCAL.get() != null;
  }

  public static void release() {
    TRACE_CONTEXT_THREAD_LOCAL.remove();
  }

  @Nonnull
  public static Optional<TraceContext> current() {
    return Optional.ofNullable(TRACE_CONTEXT_THREAD_LOCAL.get());
  }

  private TraceContextHolder() {
  }
}
