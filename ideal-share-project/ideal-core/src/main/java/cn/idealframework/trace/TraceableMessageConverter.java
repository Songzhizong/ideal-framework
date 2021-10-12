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

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import cn.idealframework.date.DateTimes;
import lombok.Setter;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2021/1/12
 */
public class TraceableMessageConverter extends ClassicConverter {
  @Setter
  private static LogCollector logCollector = null;

  @Override
  public String convert(@Nonnull ILoggingEvent iLoggingEvent) {
    String formattedMessage = iLoggingEvent.getFormattedMessage();
    if (TraceContextHolder.isInitialized()) {
      TraceContext context = TraceContextHolder.current().orElse(null);
      if (context != null) {
        if (logCollector != null) {
          LogInfo logInfo = iLoggingEventToCollectLog(context, iLoggingEvent);
          logCollector.collect(logInfo);
        }
        return context.getLogPrefix() + formattedMessage;
      }
    }
    return formattedMessage;
  }

  @Nonnull
  private LogInfo iLoggingEventToCollectLog(@Nonnull TraceContext context, @Nonnull ILoggingEvent iLoggingEvent) {
    String traceId = context.getTraceId();
    String spanId = context.getSpanId();
    LogInfo logInfo = new LogInfo();
    logInfo.setTraceId(traceId);
    logInfo.setSpanId(spanId);
    logInfo.setLoggerName(iLoggingEvent.getLoggerName());
    logInfo.setThreadName(iLoggingEvent.getThreadName());
    logInfo.setLevel(iLoggingEvent.getLevel().levelStr);
    logInfo.setMessage(iLoggingEvent.getFormattedMessage());
    logInfo.setLogTime(DateTimes.parse(iLoggingEvent.getTimeStamp()));
    return logInfo;
  }
}
