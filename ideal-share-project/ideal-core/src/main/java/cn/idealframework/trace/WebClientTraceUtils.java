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

import org.springframework.http.HttpHeaders;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

/**
 * @author 宋志宗 on 2021/7/4
 */
public final class WebClientTraceUtils {

  @Nonnull
  public static Consumer<TraceContext> setTraceHeaders(@Nonnull HttpHeaders httpHeaders) {
    return traceContext -> {
      setTraceHeaders(httpHeaders, traceContext);
    };
  }

  public static void setTraceHeaders(@Nonnull HttpHeaders httpHeaders,
                                     @Nonnull TraceContext traceContext) {
    String traceId = traceContext.getTraceId();
    String spanId = traceContext.generateNextSpanId();
    httpHeaders.set(TraceConstants.HTTP_HEADER_TRACE_ID, traceId);
    httpHeaders.set(TraceConstants.HTTP_HEADER_SPAN_ID, spanId);
  }

  private WebClientTraceUtils() {
  }
}
