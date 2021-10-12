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
package cn.idealframework.authentication;

import cn.idealframework.extensions.ServletUtils;
import cn.idealframework.lang.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Set;

/**
 * @author 宋志宗 on 2021/6/23
 */
@CommonsLog
@RequiredArgsConstructor
public class BasicAuthenticator implements Authenticator {
  public static final String SIGNER_TYPE = "Basic";
  public static final String AK_HEADER_NAME = "Ideal-Access-Key";
  public static final String REQUEST_DATE_HEADER_NAME = "Request-Date";
  public static final String SECRET_TOKEN_HEADER_NAME = "Ideal-Secret-Token";

  private final CertificateRepository certificateRepository;

  @Nonnull
  @Override
  public String getSignerType() {
    return SIGNER_TYPE;
  }

  @Override
  public void authenticate(@Nonnull HttpServletRequest request) throws AuthenticateException {
    String accessKey = request.getHeader(AK_HEADER_NAME);
    String requestDate = request.getHeader(REQUEST_DATE_HEADER_NAME);
    String secretToken = request.getHeader(SECRET_TOKEN_HEADER_NAME);
    String originalIp = ServletUtils.getOriginalIp(request);
    authenticate(accessKey, requestDate, secretToken, originalIp);
  }

  public void authenticate(@Nonnull String accessKey,
                           @Nonnull String requestDate,
                           @Nonnull String secretToken,
                           @Nonnull String originalIp) throws AuthenticateException {
    if (StringUtils.isBlank(accessKey)) {
      log.info("Access key header is blank");
      throw new AuthenticateException("Access key header is blank");
    }
    if (StringUtils.isBlank(requestDate)) {
      log.info("Request date header is blank");
      throw new AuthenticateException("Request date header is blank");
    }
    if (StringUtils.isBlank(secretToken)) {
      log.info("Authorization header is blank");
      throw new AuthenticateException("Authorization header is blank");
    }
    // 1. ak认证
    Certificate certificate = certificateRepository.getCertificate(accessKey)
        .orElseThrow(() -> {
          log.info("Invalid access key: " + accessKey);
          return new AuthenticateException("Invalid access key");
        });
    // 2. 有效期认证
    long expireTimestamp = certificate.getExpireTimestamp();
    if (expireTimestamp > 0 && expireTimestamp < System.currentTimeMillis()) {
      log.info("Certificate has expired: " + accessKey);
      throw new AuthenticateException("Certificate has expired");
    }
    // 3. 客户端ip认证
    Set<String> allowIps = certificate.getAllowIps();
    if (allowIps != null && !allowIps.isEmpty()) {
      if (!allowIps.contains(originalIp)) {
        log.info("Illegal client ip address: " + originalIp);
        throw new AuthenticateException("Illegal client ip address");
      }
    }
    // 4. secret token认证
    // 4.1 获取请求时间戳
    LocalDateTime dateTime;
    try {
      dateTime = LocalDateTime.parse(requestDate, DateTimeFormatter.ISO_DATE_TIME);
    } catch (Exception e) {
      log.info("Illegal request date header" + requestDate);
      throw new AuthenticateException("Illegal request date header");
    }
    long timestamp = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
    //
    String secretKey = certificate.getSecretKey();
    String calculateSecretToken = BasicAuthenticationUtils.calculateSecretToken(timestamp, accessKey, secretKey);
    if (!secretToken.equals(calculateSecretToken)) {
      log.info("Illegal secret token: " + secretToken + " expected is " + calculateSecretToken);
      throw new AuthenticateException("Illegal secret token");
    }
  }
}
