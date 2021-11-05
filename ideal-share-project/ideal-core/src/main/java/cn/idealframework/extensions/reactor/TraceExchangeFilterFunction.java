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

import cn.idealframework.trace.TraceContext;
import cn.idealframework.trace.TraceContextHolder;
import cn.idealframework.trace.WebClientTraceUtils;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2021/11/5
 */
public class TraceExchangeFilterFunction implements ExchangeFilterFunction {
  private static final ExchangeFilterFunction INSTANCE = new TraceExchangeFilterFunction();

  private TraceExchangeFilterFunction() {
  }

  @Nonnull
  public static ExchangeFilterFunction getInstance() {
    return INSTANCE;
  }

  @Nonnull
  @Override
  public Mono<ClientResponse> filter(@Nonnull ClientRequest request,
                                     @Nonnull ExchangeFunction next) {
    TraceContext context = TraceContextHolder.current().orElse(null);
    if (context != null) {
      ClientRequest clientRequest = ClientRequest.from(request)
        .headers(headers -> WebClientTraceUtils.setTraceHeaders(headers, context))
        .build();
      return next.exchange(clientRequest);
    }
    return next.exchange(request);
  }
}
