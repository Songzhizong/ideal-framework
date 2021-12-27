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

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2020/12/20
 */
public class BadRequestException extends VisibleRuntimeException {
  private static final long serialVersionUID = 6232518534259102606L;

  public BadRequestException(int code, @Nonnull String message) {
    super(ResMsg.BAD_REQUEST.httpStatus(), code, message);
  }

  public BadRequestException(@Nonnull String message) {
    super(ResMsg.BAD_REQUEST.httpStatus(), ResMsg.BAD_REQUEST.code(), message);
  }
}
