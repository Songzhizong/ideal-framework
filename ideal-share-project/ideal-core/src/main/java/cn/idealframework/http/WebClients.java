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
package cn.idealframework.http;

import cn.idealframework.extensions.reactor.Reactors;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.annotation.Nonnull;
import java.time.Duration;

/**
 * @author 宋志宗 on 2021/3/8
 * @deprecated see {@link  Reactors}
 */
@Deprecated
public class WebClients {

  @Nonnull
  public static WebClient createWebClient(@Nonnull Duration responseTimeout) {
    return createWebClientBuilder(responseTimeout).build();
  }

  @Nonnull
  public static WebClient.Builder createWebClientBuilder(@Nonnull Duration responseTimeout) {
    HttpClient httpClient = createHttpClient(responseTimeout);
    return WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient));
  }

  @Nonnull
  public static HttpClient createHttpClient(@Nonnull Duration responseTimeout) {
    return HttpClient.create().responseTimeout(responseTimeout).keepAlive(true);
  }

  private WebClients() {
  }
}
