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
import io.netty.handler.timeout.ReadTimeoutException;
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
 * @author 宋志宗 on 2021/7/4
 */
@Order(Ordered.LOWEST_PRECEDENCE - 3)
@CommonsLog
@Configuration
@ControllerAdvice
@RequiredArgsConstructor
@ConditionalOnClass({WebModule.class, ReadTimeoutException.class})
@ConditionalOnExpression("${ideal.web.enable-unified-exception-handler:true}")
public class NettyExceptionHandlerAdvice {
  private static final MultiValueMap<String, String> RESPONSE_HEADERS = new LinkedMultiValueMap<>();

  static {
    RESPONSE_HEADERS.set("Content-Type", "application/json;charset=utf-8");
  }

  /**
   * http请求超时
   */
  @ExceptionHandler(ReadTimeoutException.class)
  public ResponseEntity<Object> readTimeoutExceptionHandler(@Nonnull ReadTimeoutException exception) {
    log.info("http请求超时: " + exception.getMessage());
    BasicResult res = new BasicResult();
    res.setSuccess(false);
    res.setCode(ResMsg.INTERNAL_SERVER_ERROR.code());
    res.setMessage("请求超时");
    TraceContextHolder.current().ifPresent(context -> res.setTraceId(context.getTraceId()));
    return new ResponseEntity<>(res, RESPONSE_HEADERS, HttpStatus.OK);
  }
}
