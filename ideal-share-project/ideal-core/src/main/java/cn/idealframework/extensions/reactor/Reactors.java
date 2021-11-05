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
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Duration;
import java.util.function.Consumer;

/**
 * reactor http相关
 * <pre>
 *   - 整个程序中应尽量共用同一个 http client
 * </pre>
 *
 * @author 宋志宗 on 2021/9/17
 */
@SuppressWarnings("unused")
@CommonsLog
public final class Reactors {

  @Nonnull
  public static WebClient webClient() {
    return webClient(new FullWebClientOptions());
  }

  @Nonnull
  public static WebClient webClient(@Nonnull Consumer<FullWebClientOptions> consumer) {
    FullWebClientOptions options = new FullWebClientOptions();
    consumer.accept(options);
    return webClient(options);
  }

  @Nonnull
  public static WebClient webClient(@Nonnull FullWebClientOptions options) {
    return webClientBuilder(options).build();
  }

  @Nonnull
  public static WebClient.Builder webClientBuilder(@Nonnull Consumer<FullWebClientOptions> consumer) {
    FullWebClientOptions options = new FullWebClientOptions();
    consumer.accept(options);
    return webClientBuilder(options);
  }

  @Nonnull
  public static WebClient.Builder webClientBuilder() {
    return webClientBuilder(new FullWebClientOptions());
  }

  @Nonnull
  public static WebClient.Builder webClientBuilder(@Nonnull FullWebClientOptions options) {
    HttpClient httpClient = httpClient(options.toHttpClientOptions());
    return webClientBuilder(httpClient, options.toWebClientOptions());
  }

  @Nonnull
  public static WebClient.Builder webClientBuilder(@Nonnull HttpClient httpClient) {
    return webClientBuilder(httpClient, (WebClientOptions) null);
  }

  @Nonnull
  public static WebClient.Builder webClientBuilder(@Nonnull HttpClient httpClient,
                                                   @Nonnull Consumer<WebClientOptions> consumer) {
    WebClientOptions options = new WebClientOptions();
    consumer.accept(options);
    return webClientBuilder(httpClient, options);
  }

  @Nonnull
  public static WebClient.Builder webClientBuilder(@Nonnull HttpClient httpClient,
                                                   @Nullable WebClientOptions options) {
    WebClient.Builder builder = WebClient.builder()
      .clientConnector(new ReactorClientHttpConnector(httpClient))
      .filter((request, next) -> {
        TraceContext context = TraceContextHolder.current().orElse(null);
        if (context != null) {
          ClientRequest clientRequest = ClientRequest.from(request)
            .headers(headers -> WebClientTraceUtils.setTraceHeaders(headers, context))
            .build();
          return next.exchange(clientRequest);
        }
        return next.exchange(request);
      });
    if (options != null) {
      // URI 编码方式
      DefaultUriBuilderFactory.EncodingMode encodingMode = options.getEncodingMode();
      if (encodingMode != null) {
        DefaultUriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory();
        uriBuilderFactory.setEncodingMode(encodingMode);
        builder.uriBuilderFactory(uriBuilderFactory);
      }
      // 最大内存占用
      Integer maxInMemorySize = options.getMaxInMemorySize();
      if (maxInMemorySize != null) {
        builder.codecs(c -> c.defaultCodecs().maxInMemorySize(maxInMemorySize));
      }
    }
    return builder;
  }

  @Nonnull
  public static HttpClient httpClient() {
    return httpClient(new HttpClientOptions());
  }

  @Nonnull
  public static HttpClient httpClient(@Nonnull Consumer<HttpClientOptions> consumer) {
    HttpClientOptions options = new HttpClientOptions();
    consumer.accept(options);
    return httpClient(options);
  }

  @Nonnull
  public static HttpClient httpClient(@Nonnull HttpClientOptions options) {
    ConnectionProvider.Builder builder = ConnectionProvider
      .builder(options.getName())
      .maxConnections(options.getMaxConnections());
    Integer pendingAcquireMaxCount = options.getPendingAcquireMaxCount();
    if (pendingAcquireMaxCount != null && pendingAcquireMaxCount > 0) {
      builder.pendingAcquireMaxCount(pendingAcquireMaxCount);
    }
    ConnectionProvider connectionProvider = builder.build();
    HttpClient httpClient = HttpClient.create(connectionProvider)
      .keepAlive(options.isKeepAlive())
      .followRedirect(options.isFollowRedirect())
      .compress(options.isCompressionEnabled());
    Duration responseTimeout = options.getResponseTimeout();
    if (responseTimeout != null && !responseTimeout.isZero()) {
      httpClient.responseTimeout(responseTimeout);
    }
    return httpClient;
  }
}
