///*
// * Copyright 2021 cn.idealframework
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      https://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package cn.idealframework.operation;
//
//import cn.idealframework.trace.TraceContext;
//import cn.idealframework.trace.TraceContextHolder;
//import cn.idealframework.trace.WebClientTraceUtils;
//import cn.idealframework.util.Asserts;
//import lombok.extern.apachecommons.CommonsLog;
//import org.springframework.http.MediaType;
//import org.springframework.web.reactive.function.BodyInserters;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Mono;
//import reactor.util.retry.Retry;
//
//import javax.annotation.Nonnull;
//import javax.annotation.Nullable;
//import java.time.Duration;
//import java.util.Optional;
//
///**
// * @author 宋志宗 on 2021/6/4
// */
//@CommonsLog
//public class OperationLogStorageWebClientImpl implements OperationLogStorage {
//  /** 是否异步执行 */
//  private final boolean async;
//  /** 推送url */
//  private final String url;
//  /** webClient */
//  private final WebClient webClient;
//  /** 最大失败重试次数 */
//  private final int retryMaxAttempts;
//  /** 失败重试间隔 */
//  private final Duration retryFixedDelay;
//
//  public OperationLogStorageWebClientImpl(boolean async, @Nonnull String url, @Nonnull WebClient webClient) {
//    this(async, url, webClient, null, null);
//  }
//
//  public OperationLogStorageWebClientImpl(boolean async,
//                                          @Nonnull String url,
//                                          @Nonnull WebClient webClient,
//                                          @Nullable Integer retryMaxAttempts,
//                                          @Nullable Duration retryFixedDelay) {
//    Asserts.notBlank(url, "url must be not blank");
//    Asserts.nonnull(webClient, "webClient must be not blank");
//    // 异步最大重试5次, 同步最大重试3次
//    if (retryMaxAttempts == null) {
//      retryMaxAttempts = async ? 5 : 3;
//    }
//    // 异步延迟200ms, 同步延迟10ms
//    if (retryFixedDelay == null) {
//      retryFixedDelay = async ? Duration.ofMillis(200) : Duration.ofMillis(10);
//    }
//    this.async = async;
//    this.url = url;
//    this.webClient = webClient;
//    this.retryMaxAttempts = retryMaxAttempts;
//    this.retryFixedDelay = retryFixedDelay;
//  }
//
//  @Override
//  public void save(@Nonnull OperationLog operationLog) {
//    Optional<TraceContext> current = TraceContextHolder.current();
//    Mono<String> mono = webClient.post().uri(url)
//      .contentType(MediaType.APPLICATION_JSON)
//      .headers(headers -> current.ifPresent(WebClientTraceUtils.setTraceHeaders(headers)))
//      .body(BodyInserters.fromValue(operationLog))
//      .retrieve()
//      .bodyToMono(String.class)
//      .doOnNext(r -> log.debug("Save operation log result: " + r));
//    Mono<String> retry;
//    if (retryMaxAttempts > 0) {
//      retry = mono.retryWhen(Retry.fixedDelay(retryMaxAttempts, retryFixedDelay));
//    } else {
//      retry = mono;
//    }
//    Mono<String> resume = retry.onErrorResume(throwable -> {
//      String message = throwable.getMessage();
//      log.info("Save operation log ex: " + message);
//      return Mono.just(message);
//    });
//    if (async) {
//      resume.subscribe();
//    } else {
//      resume.block();
//    }
//  }
//}
