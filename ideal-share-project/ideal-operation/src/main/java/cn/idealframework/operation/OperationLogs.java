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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * @author 宋志宗 on 2021/6/4
 */
public final class OperationLogs {
  private static final ThreadLocal<OperationLog> OPERATION_LOG_THREAD_LOCAL = new ThreadLocal<>();

  private OperationLogs() {
  }

  /** 获取日志对象 */
  @Nonnull
  public static Optional<OperationLog> current() {
    return Optional.ofNullable(OPERATION_LOG_THREAD_LOCAL.get());
  }

  static void release() {
    OPERATION_LOG_THREAD_LOCAL.remove();
  }

  @Nonnull
  static OperationLog init() {
    OperationLog operationLog = new OperationLog();
    OPERATION_LOG_THREAD_LOCAL.set(operationLog);
    return operationLog;
  }

  /**
   * 标记处理失败
   *
   * @param message 失败信息
   * @author 宋志宗 on 2021/6/4
   */
  public static void markFailure(@Nullable String message) {
    current().ifPresent(operationLog -> {
      operationLog.setSuccess(false);
      operationLog.setMessage(message);
    });
  }

  /**
   * 设置操作详情
   *
   * @param details 操作详情
   * @author 宋志宗 on 2021/6/4
   */
  public static void details(@Nonnull String details) {
    current().ifPresent(operationLog -> operationLog.setDetails(details));
  }
}
