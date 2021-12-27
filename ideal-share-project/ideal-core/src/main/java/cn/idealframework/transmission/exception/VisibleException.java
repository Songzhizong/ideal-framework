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
package cn.idealframework.transmission.exception;

import cn.idealframework.transmission.ResMsg;
import lombok.Getter;

import javax.annotation.Nonnull;

/**
 * 可作为响应内容的可见性受检异常
 *
 * @author 宋志宗 on 2020/12/20
 */
@Getter
public class VisibleException extends Exception implements ResMsg {
  private static final long serialVersionUID = -6281199227602676282L;

  private final int httpStatus;

  private final int code;
  @Nonnull
  private final String message;

  public VisibleException(@Nonnull ResMsg resMsg) {
    super(resMsg.message());
    this.httpStatus = resMsg.httpStatus();
    this.code = resMsg.code();
    this.message = resMsg.message();
  }

  public VisibleException(int httpStatus, int code, @Nonnull String message) {
    super(message);
    this.httpStatus = httpStatus;
    this.code = code;
    this.message = message;
  }

  @Override
  public int httpStatus() {
    return httpStatus;
  }

  @Override
  public int code() {
    return code;
  }

  @Nonnull
  @Override
  public String message() {
    return message;
  }
}
