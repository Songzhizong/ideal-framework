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
package cn.idealframework.boot.autoconfigure.web;

import cn.idealframework.boot.starter.module.lock.LockModule;
import cn.idealframework.lock.DistributedLockConflictException;
import cn.idealframework.trace.TraceContextHolder;
import cn.idealframework.transmission.ResMsg;
import cn.idealframework.transmission.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2021/9/4
 */
@SuppressWarnings("DuplicatedCode")
@Order(Ordered.LOWEST_PRECEDENCE - 4)
@CommonsLog
@Configuration
@ControllerAdvice
@RequiredArgsConstructor
@ConditionalOnClass({LockModule.class, DistributedLockConflictException.class})
@ConditionalOnExpression("${ideal.web.enable-unified-exception-handler:true}")
public class IdealLockExceptionHandlerAdvice {
  private static final MultiValueMap<String, String> RESPONSE_HEADERS = new LinkedMultiValueMap<>();

  static {
    RESPONSE_HEADERS.set("Content-Type", "application/json;charset=utf-8");
  }


  @Nonnull
  @ExceptionHandler(DistributedLockConflictException.class)
  public ResponseEntity<Object> distributedLockConflictExceptionHandler(@Nonnull DistributedLockConflictException ex) {
    Result<Object> body = Result.exception(ex);
    body.setHttpStatus(ResMsg.INTERNAL_SERVER_ERROR.httpStatus());
    String message = ex.getMessage();
    TraceContextHolder.current().ifPresent(context -> body.setTraceId(context.getTraceId()));
    log.info(message);
    return new ResponseEntity<>(body, RESPONSE_HEADERS, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
