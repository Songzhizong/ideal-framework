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
package cn.idealframework.transmission;

import cn.idealframework.lang.StringUtils;
import cn.idealframework.trace.TraceContextHolder;
import cn.idealframework.transmission.exception.ResultException;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.apachecommons.CommonsLog;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.beans.Transient;
import java.io.Serializable;

/**
 * @author 宋志宗 on 2021/8/25
 */
@Getter
@Setter
@CommonsLog
@Accessors(chain = true)
public class BasicResult implements ResMsg, Serializable {
  private static final long serialVersionUID = 1658084050565123764L;

  @Nullable
  private String traceId;

  private boolean success = true;

  private int httpStatus = 200;

  private int code = 200;

  @Nonnull
  private String message = "success";

  public BasicResult() {
    TraceContextHolder.current()
      .ifPresent(traceContext -> this.setTraceId(traceContext.getTraceId()));
  }

  @Transient
  public boolean isFailure() {
    return !isSuccess();
  }

  public void setMessage(@Nullable String message) {
    if (StringUtils.isBlank(message)) {
      message = "";
    }
    this.message = message;
  }

  /**
   * 当调用失败时抛出 {@link ResultException}
   */
  public void onFailureThrow() throws ResultException {
    if (isFailure()) {
      int httpStatus = getHttpStatus();
      int code = getCode();
      String message = getMessage();
      log.info("Result is failure, httpStatus: "
        + httpStatus + " code: " + code + " message: " + message);
      throw new ResultException(httpStatus, code, message);
    }
  }

  @Override
  public int code() {
    return getCode();
  }

  @Nonnull
  @Override
  public String message() {
    return getMessage();
  }
}
