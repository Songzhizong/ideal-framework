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
package cn.idealframework.boot.autoconfigure.authentication;

import cn.idealframework.authentication.*;
import cn.idealframework.boot.starter.module.authentication.AuthenticationModule;
import cn.idealframework.util.Asserts;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Nonnull;
import javax.servlet.Filter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 宋志宗 on 2021/6/24
 */
@CommonsLog
@Configuration
@RequiredArgsConstructor
@ConditionalOnClass({AuthenticationModule.class, Filter.class})
@EnableConfigurationProperties(IdealBootAuthenticationProperties.class)
public class IdealBootAuthenticationAutoConfigure {
  private final IdealBootAuthenticationProperties properties;

  @Bean
  @ConditionalOnMissingBean
  public CertificateRepository certificateRepository() {
    Set<Certificate> certificates = properties.getCertificates();
    Map<String, Certificate> certificateMap = certificates.stream()
        .collect(Collectors.toMap(Certificate::getAccessKey, v -> v));
    return new MemoryCertificateRepository(certificateMap);
  }

  @Bean
  public AuthenticatorRegistry authenticatorRegistry() {
    return new AuthenticatorRegistryImpl();
  }

  @Bean
  public BasicAuthenticator basicAuthenticator(@Nonnull AuthenticatorRegistry authenticatorRegistry,
                                               @Nonnull CertificateRepository certificateRepository) {
    Asserts.nonnull(certificateRepository, "CertificateRepository must be not null");
    BasicAuthenticator basicAuthenticator = new BasicAuthenticator(certificateRepository);
    authenticatorRegistry.register(basicAuthenticator);
    return basicAuthenticator;
  }

  @Bean
  public List<Authenticator> authenticators(@Nonnull CertificateRepository certificateRepository) {
    Asserts.nonnull(certificateRepository, "CertificateRepository must be not null");
    BasicAuthenticator basicAuthenticator = new BasicAuthenticator(certificateRepository);
    return Collections.singletonList(basicAuthenticator);
  }

  @Bean
  public AuthenticateFilter authenticateFilter(@Nonnull AuthenticatorRegistry authenticatorRegistry) {
    Set<String> patterns = properties.getMatchPatterns();
    return new AuthenticateFilter(patterns, authenticatorRegistry);
  }
}
