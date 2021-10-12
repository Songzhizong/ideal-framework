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

/**
 * @author 宋志宗 on 2021/6/5
 */
public final class TraceConstants {
  public static final String HTTP_HEADER_TRACE_ID = "X-Ideal-Trace-ID";
  public static final String HTTP_HEADER_SPAN_ID = "X-Ideal-Span-ID";

  public static final String REQUEST_ATTR_NAME_TRACE = "ideal.trace.context";

  private TraceConstants() {
  }
}
