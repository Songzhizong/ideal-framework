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
package cn.idealframework.trace.impl;

import cn.idealframework.json.JsonUtils;
import cn.idealframework.trace.TraceCollector;
import cn.idealframework.trace.TraceInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2021/1/12
 */
@CommonsLog
@RequiredArgsConstructor
public class LogTraceCollector implements TraceCollector {
  private final boolean pretty;

  @Override
  public void collect(@Nonnull TraceInfo traceInfo) {
    if (log.isInfoEnabled()) {
      if (pretty) {
        log.info("TraceLog: " + JsonUtils.toPrettyJsonString(traceInfo));

      } else {
        log.info("TraceLog: " + JsonUtils.toJsonString(traceInfo));
      }
    }
  }
}
