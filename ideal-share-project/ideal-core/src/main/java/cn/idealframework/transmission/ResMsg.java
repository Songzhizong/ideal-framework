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

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2019-05-14
 */
public interface ResMsg {

  default int httpStatus() {
    return 200;
  }

  /**
   * 返回码
   */
  int code();

  /**
   * 响应描述
   */
  @Nonnull
  String message();

  ResMsg SUCCESS = StandardResMsg.SUCCESS;
  ResMsg BAD_REQUEST = StandardResMsg.BAD_REQUEST;
  ResMsg UNAUTHORIZED = StandardResMsg.UNAUTHORIZED;
  ResMsg FORBIDDEN = StandardResMsg.FORBIDDEN;
  ResMsg NOT_FOUND = StandardResMsg.NOT_FOUND;
  ResMsg METHOD_NOT_ALLOWED = StandardResMsg.METHOD_NOT_ALLOWED;
  ResMsg INTERNAL_SERVER_ERROR = StandardResMsg.INTERNAL_SERVER_ERROR;

  @Getter
  @AllArgsConstructor
  enum StandardResMsg implements ResMsg {
    SUCCESS(200, 200, "Success"),
    BAD_REQUEST(200, 400, "Bad request"),
    UNAUTHORIZED(200, 401, "Unauthorized"),
    FORBIDDEN(200, 403, "Forbidden"),
    NOT_FOUND(200, 404, "Not found"),
    METHOD_NOT_ALLOWED(200, 405, "Method Not Allowed"),
    INTERNAL_SERVER_ERROR(200, 500, "Internal Server Error"),
    ;

    private final int httpStatus;

    private final int code;
    @Nonnull
    private final String message;


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
}
