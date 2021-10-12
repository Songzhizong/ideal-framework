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

/**
 * @author 宋志宗 on 2021/6/4
 */
public final class OperationLogHolder {
  private static final ThreadLocal<OperationLog> OPERATION_LOG_THREAD_LOCAL = ThreadLocal.withInitial(OperationLog::new);

  /** 获取日志对象 */
  @Nonnull
  public static OperationLog get() {
    return OPERATION_LOG_THREAD_LOCAL.get();
  }

  static void release() {
    OPERATION_LOG_THREAD_LOCAL.remove();
  }

  /**
   * 标记处理失败
   *
   * @param message 失败信息
   * @author 宋志宗 on 2021/6/4
   */
  public static void markFailure(@Nullable String message) {
    OperationLog operationLog = get();
    operationLog.setSuccess(false);
    operationLog.setMessage(message);
  }

  /**
   * 设置操作描述
   *
   * @param description 操作描述
   * @author 宋志宗 on 2021/6/4
   */
  public static void description(@Nonnull String description) {
    OperationLog operationLog = get();
    operationLog.setDescription(description);
  }


  private OperationLogHolder() {
  }
}
