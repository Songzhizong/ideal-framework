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

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;

/**
 * @author 宋志宗 on 2021/6/4
 */
@FunctionalInterface
public interface PreFilterHandler {

  /**
   * 在traceLog过滤器执行前的操作
   * 可以在此对TraceContext进行一些初始化处理, 例如从请求中获取某些信息写入TraceContext
   *
   * @param request HttpServletRequest
   * @param context TraceContext
   * @throws Exception Exception
   * @author 宋志宗 on 2021/6/4
   */
  void handle(@Nonnull HttpServletRequest request,
              @Nonnull TraceContext context) throws Exception;
}
