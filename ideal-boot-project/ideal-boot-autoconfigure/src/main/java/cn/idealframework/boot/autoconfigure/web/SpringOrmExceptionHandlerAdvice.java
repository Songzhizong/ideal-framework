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

import cn.idealframework.boot.starter.module.web.WebModule;
import cn.idealframework.trace.TraceContextHolder;
import cn.idealframework.transmission.BasicResult;
import cn.idealframework.transmission.ResMsg;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2021/7/4
 */
@SuppressWarnings("DuplicatedCode")
@Order(Ordered.LOWEST_PRECEDENCE - 1)
@CommonsLog
@Configuration
@ControllerAdvice
@RequiredArgsConstructor
@ConditionalOnExpression("${ideal.web.enable-unified-exception-handler:true}")
@ConditionalOnClass({WebModule.class, ObjectOptimisticLockingFailureException.class})
public class SpringOrmExceptionHandlerAdvice {
  private static final MultiValueMap<String, String> RESPONSE_HEADERS = new LinkedMultiValueMap<>();

  static {
    RESPONSE_HEADERS.set("Content-Type", "application/json;charset=utf-8");
  }

  /**
   * 乐观锁冲突
   */
  @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
  public ResponseEntity<Object> objectOptimisticLockingFailureExceptionHandler(
      @Nonnull ObjectOptimisticLockingFailureException e) {
    log.info("乐观锁冲突: " + e.getMessage());
    BasicResult res = new BasicResult();
    res.setSuccess(false);
    res.setCode(ResMsg.INTERNAL_SERVER_ERROR.code());
    res.setMessage("Object optimistic locking failure.");
    TraceContextHolder.current().ifPresent(context -> res.setTraceId(context.getTraceId()));
    return new ResponseEntity<>(res, RESPONSE_HEADERS, HttpStatus.OK);
  }
}
