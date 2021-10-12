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
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.annotation.Nonnull;
import java.sql.SQLException;

/**
 * @author 宋志宗 on 2021/7/4
 */
@SuppressWarnings("DuplicatedCode")
@Order(Ordered.LOWEST_PRECEDENCE - 2)
@CommonsLog
@Configuration
@ControllerAdvice
@RequiredArgsConstructor
@ConditionalOnClass({WebModule.class, DataIntegrityViolationException.class})
@ConditionalOnExpression("${ideal.web.enable-unified-exception-handler:true}")
public class SpringJdbcExceptionHandlerAdvice {
  private static final MultiValueMap<String, String> RESPONSE_HEADERS = new LinkedMultiValueMap<>();

  static {
    RESPONSE_HEADERS.set("Content-Type", "application/json;charset=utf-8");
  }

  /**
   * jdbc异常
   */
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<Object> dataIntegrityViolationExceptionHandler(
      @Nonnull DataIntegrityViolationException exception) {
    String message = exception.getMessage();
    Throwable cause = exception.getCause();
    if (cause instanceof ConstraintViolationException) {
      ConstraintViolationException ex = (ConstraintViolationException) cause;
      String constraintName = ex.getConstraintName();
      SQLException sqlException = ex.getSQLException();
      int errorCode = sqlException.getErrorCode();
      // 数据重复,唯一索引冲突
      if (errorCode == 1062) {
        log.info(constraintName + " 已存在");
        message = constraintName + "已存在";
      }
    }
    if (message == null) {
      message = exception.getClass().getSimpleName();
    }
    log.info(message);
    BasicResult res = new BasicResult();
    res.setSuccess(false);
    res.setCode(ResMsg.BAD_REQUEST.code());
    res.setMessage(message);
    TraceContextHolder.current().ifPresent(context -> res.setTraceId(context.getTraceId()));
    return new ResponseEntity<>(res, RESPONSE_HEADERS, HttpStatus.OK);
  }

  @Nonnull
  @ExceptionHandler(InvalidDataAccessApiUsageException.class)
  public ResponseEntity<Object> invalidDataAccessApiUsageExceptionHandler(
      @Nonnull InvalidDataAccessApiUsageException ex) {
    String message = ex.getMessage();
    Throwable cause = ex.getCause();
    if (cause != null) {
      String message1 = cause.getMessage();
      if (message1 != null) {
        message = message1;
      }
    }
    if (message == null) {
      message = "InvalidDataAccessApiUsageException";
    }
    log.info(message);
    BasicResult res = new BasicResult();
    res.setSuccess(false);
    res.setCode(ResMsg.BAD_REQUEST.code());
    res.setMessage(message);
    TraceContextHolder.current().ifPresent(context -> res.setTraceId(context.getTraceId()));
    return new ResponseEntity<>(res, RESPONSE_HEADERS, HttpStatus.OK);
  }

}
