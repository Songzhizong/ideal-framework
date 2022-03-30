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
package cn.idealframework.boot.autoconfigure.operation;

import cn.idealframework.boot.autoconfigure.operation.properties.IdealBootOperationProperties;
import cn.idealframework.boot.autoconfigure.operation.properties.StorageProperties;
import cn.idealframework.boot.autoconfigure.operation.properties.StorageType;
import cn.idealframework.boot.autoconfigure.operation.properties.WebClientStorageProperties;
import cn.idealframework.boot.starter.module.operation.OperationModule;
import cn.idealframework.extensions.reactor.Reactors;
import cn.idealframework.operation.OperationLogAspect;
import cn.idealframework.operation.OperationLogStorage;
import cn.idealframework.operation.OperationLogStorageWebClientImpl;
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

import javax.annotation.Nullable;
import java.time.Duration;

/**
 * @author 宋志宗 on 2021/6/5
 */
@CommonsLog
@Configuration
@RequiredArgsConstructor
@ConditionalOnClass({OperationLogAspect.class, OperationModule.class})
@ConditionalOnExpression("'${ideal.operation.storage.storage-type}'.equalsIgnoreCase('web_client')&&${ideal.operation.storage.web-client.load-balance:false}")
public class IdealBootLbWebClientOperationLogStorageAutoConfigure {
  private final IdealBootOperationProperties properties;

  @Bean
  @ConditionalOnMissingBean
  public OperationLogStorage operationLogStorage(
    @Nullable @Autowired(required = false)
      ReactorLoadBalancerExchangeFilterFunction lbFunction) {
    if (lbFunction == null) {
      throw new IllegalArgumentException("ReactorLoadBalancerExchangeFilterFunction is null");
    }
    StorageProperties storage = properties.getStorage();
    StorageType storageType = storage.getType();
    if (storageType == StorageType.WEB_CLIENT) {
      WebClientStorageProperties webClientStorageProperties = storage.getWebClient();
      String url = webClientStorageProperties.getUrl();
      log.info("Initializing loadBalanced OperationLogStorageWebClientImpl");
      WebClient webClient = Reactors
        .webClientBuilder(ops -> ops.setResponseTimeout(Duration.ofSeconds(5)))
        .filter(lbFunction).build();
      return new OperationLogStorageWebClientImpl(true, url, webClient);
    }
    return null;
  }
}
