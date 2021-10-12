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

import org.junit.Test;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * @author 宋志宗 on 2021/6/23
 */
public class BasicAuthenticatorTest {
  private static final String ACCESS_KEY = "accessKey";
  private static final String SECRET_KEY = "secretKey";
  private static final BasicAuthenticator AUTHENTICATOR = new BasicAuthenticator(accessKey -> {
    Certificate certificate = Certificate.builder()
        .accessKey(ACCESS_KEY)
        .secretKey(SECRET_KEY)
        .expireTimestamp(System.currentTimeMillis() << 1)
        .allowIps(Collections.singleton("192.168.1.181"))
        .build();
    return Optional.of(certificate);
  });

  @Test
  public void test() throws AuthenticateException {
    Map<String, String> map = BasicAuthenticationUtils.generateSignatureHeaders(ACCESS_KEY, SECRET_KEY);
    map.forEach((k, v) -> System.out.println(k + " : " + v));
    String requestDate = map.get(BasicAuthenticator.REQUEST_DATE_HEADER_NAME);
    String secretToken = map.get(BasicAuthenticator.SECRET_TOKEN_HEADER_NAME);
    AUTHENTICATOR.authenticate(ACCESS_KEY, requestDate, secretToken, "192.168.1.181");
  }

  @Test
  public void testGenerateSecretTokenSalt() {
    long currentTimeMillis = System.currentTimeMillis();
    String secret = BasicAuthenticationUtils.generateSecretTokenSalt(currentTimeMillis);
    System.out.println(currentTimeMillis);
    System.out.println(secret);
  }

  @Test
  public void testCalculateSecretToken() {
    long currentTimeMillis = System.currentTimeMillis();
    String securityToken = BasicAuthenticationUtils
        .calculateSecretToken(1624501566434L, ACCESS_KEY, SECRET_KEY);
    System.out.println(currentTimeMillis);
    System.out.println(securityToken);
  }

  @Test
  public void testFormatDate() {
    String formatDate = BasicAuthenticationUtils.formatDate(1624501566434L);
    System.out.println(formatDate);
  }
}