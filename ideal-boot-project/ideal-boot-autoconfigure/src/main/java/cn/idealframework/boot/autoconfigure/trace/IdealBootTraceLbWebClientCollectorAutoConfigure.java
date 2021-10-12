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
package cn.idealframework.boot.autoconfigure.trace;

import cn.idealframework.boot.autoconfigure.trace.properties.CollectorProperties;
import cn.idealframework.boot.autoconfigure.trace.properties.IdealBootTraceProperties;
import cn.idealframework.boot.autoconfigure.trace.properties.WebClientCollectorProperties;
import cn.idealframework.boot.starter.module.operation.TraceModule;
import cn.idealframework.http.WebClients;
import cn.idealframework.trace.LogCollector;
import cn.idealframework.trace.TraceCollector;
import cn.idealframework.trace.impl.WebClientLogCollector;
import cn.idealframework.trace.impl.WebClientTraceCollector;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Duration;

/**
 * @author 宋志宗 on 2021/6/10
 */
@CommonsLog
@Configuration
@RequiredArgsConstructor
@ConditionalOnClass(TraceModule.class)
@ConditionalOnExpression("'${ideal.trace.collector.collect-type}'.equalsIgnoreCase('WEB_CLIENT')&&${ideal.trace.collector.web-client.load-balance:false}")
public class IdealBootTraceLbWebClientCollectorAutoConfigure {

  @Bean
  @ConditionalOnMissingBean
  public TraceCollector traceCollector(@Nonnull IdealBootTraceProperties properties,
                                       @Nullable @Autowired(required = false)
                                           ReactorLoadBalancerExchangeFilterFunction lbFunction) {
    if (lbFunction == null) {
      throw new IllegalArgumentException("ReactorLoadBalancerExchangeFilterFunction is null");
    }
    CollectorProperties collector = properties.getCollector();
    CollectorProperties.CollectType collectType = collector.getCollectType();
    if (collectType == CollectorProperties.CollectType.WEB_CLIENT) {
      WebClientCollectorProperties webClientProperties = collector.getWebClient();
      if (webClientProperties.isEnableTraceCollector()) {
        String traceCollectorUrl = webClientProperties.getTraceCollectorUrl();
        WebClient webClient = WebClients.createWebClientBuilder(Duration.ofSeconds(5))
            .filter(lbFunction).build();
        log.info("Initializing loadBalanced WebClientTraceCollector");
        return new WebClientTraceCollector(true, traceCollectorUrl, webClient);
      } else {
        log.info("Trace collector is disabled");
      }
    }
    return null;
  }

  @Bean
  @ConditionalOnMissingBean
  public LogCollector logCollector(@Nonnull IdealBootTraceProperties properties,
                                   @Nullable @Autowired(required = false)
                                       ReactorLoadBalancerExchangeFilterFunction lbFunction) {
    if (lbFunction == null) {
      throw new IllegalArgumentException("ReactorLoadBalancerExchangeFilterFunction is null");
    }
    CollectorProperties collector = properties.getCollector();
    CollectorProperties.CollectType collectType = collector.getCollectType();
    if (collectType == CollectorProperties.CollectType.WEB_CLIENT) {
      WebClientCollectorProperties webClientProperties = collector.getWebClient();
      if (webClientProperties.isEnableLogCollector()) {
        String logCollectorUrl = webClientProperties.getLogCollectorUrl();
        WebClient webClient = WebClients.createWebClientBuilder(Duration.ofSeconds(5))
            .filter(lbFunction).build();
        log.info("Initializing loadBalanced WebClientTraceCollector");
        return new WebClientLogCollector(true, logCollectorUrl, webClient);
      } else {
        log.info("Log collector is disabled");
      }
    }
    return null;
  }
}
