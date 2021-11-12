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
package cn.idealframework.extensions.reactor;

import cn.idealframework.json.JsonUtils;
import cn.idealframework.json.TypeReference;
import cn.idealframework.transmission.BasicResult;
import cn.idealframework.transmission.PageResult;
import cn.idealframework.transmission.Result;
import cn.idealframework.transmission.exception.ResultException;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.util.function.Function;

/**
 * @author 宋志宗 on 2021/11/12
 */
@CommonsLog
public final class ReactorResults {


  private ReactorResults() {
  }

  @Nonnull
  public static <T extends BasicResult> Function<ClientResponse, Mono<T>> result(@Nonnull TypeReference<T> reference) {
    return resp -> {
      int statusCode = resp.rawStatusCode();
      return resp.bodyToMono(String.class)
        .map(result -> {
          log.debug("响应结果: " + result);
          T parse = JsonUtils.parse(result, reference);
          int httpStatus = parse.getHttpStatus();
          if (httpStatus != statusCode) {
            parse.setHttpStatus(statusCode);
          }
          return parse;
        })
        .onErrorResume(throwable -> {
          String message = throwable.getMessage();
          ResultException exception = new ResultException(500, 500, message);
          return Mono.error(exception);
        });
    };
  }

  @Nonnull
  public static <T> Function<ClientResponse, Mono<Result<T>>> result(@Nonnull Class<T> clazz) {
    return resp -> {
      int statusCode = resp.rawStatusCode();
      return resp.bodyToMono(String.class)
        .map(result -> {
          log.debug("响应结果: " + result);
          Result<T> parse = JsonUtils.parse(result, Result.class, clazz);
          int httpStatus = parse.getHttpStatus();
          if (httpStatus != statusCode) {
            parse.setHttpStatus(statusCode);
          }
          return parse;
        })
        .onErrorResume(throwable -> {
          String message = throwable.getMessage();
          ResultException exception = new ResultException(500, 500, message);
          return Mono.error(exception);
        });
    };
  }

  @Nonnull
  public static <E> Function<ClientResponse, Mono<PageResult<E>>> pageResult(@Nonnull Class<E> clazz) {
    return resp -> {
      int statusCode = resp.rawStatusCode();
      return resp.bodyToMono(String.class)
        .map(result -> {
          log.debug("响应结果: " + result);
          PageResult<E> parse = JsonUtils.parse(result, PageResult.class, clazz);
          int httpStatus = parse.getHttpStatus();
          if (httpStatus != statusCode) {
            parse.setHttpStatus(statusCode);
          }
          return parse;
        })
        .onErrorResume(throwable -> {
          String message = throwable.getMessage();
          ResultException exception = new ResultException(500, 500, message);
          return Mono.error(exception);
        });
    };
  }
}
