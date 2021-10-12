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

import cn.idealframework.crypto.HmacSHA1;
import cn.idealframework.lang.StringUtils;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 宋志宗 on 2021/6/28
 */
public final class BasicAuthenticationUtils {

  @Nonnull
  public static Map<String, String> generateSignatureHeaders(@Nonnull String accessKey,
                                                             @Nonnull String secretKey) {
    long currentTimeMillis = System.currentTimeMillis();
    return generateSignatureHeaders(currentTimeMillis, accessKey, secretKey);
  }

  @Nonnull
  public static Map<String, String> generateSignatureHeaders(long currentTimeMillis,
                                                             @Nonnull String accessKey,
                                                             @Nonnull String secretKey) {
    Map<String, String> headers = new HashMap<>();
    headers.put(AuthenticationConstants.HEADER_NAME_AK, accessKey);
    String formatDate = formatDate(currentTimeMillis);
    headers.put(AuthenticationConstants.HEADER_NAME_REQUEST_DATE, formatDate);
    String token = calculateSecretToken(currentTimeMillis, accessKey, secretKey);
    headers.put(AuthenticationConstants.HEADER_NAME_SECRET_TOKEN, token);
    return headers;
  }

  @Nonnull
  public static String calculateSecretToken(long timestamp,
                                            @Nonnull String accessKey,
                                            @Nonnull String secretKey) {
    String salt = generateSecretTokenSalt(timestamp);
    String s = accessKey + salt;
    byte[] encrypt = HmacSHA1.encode(s, secretKey);
    return Base64.getEncoder().encodeToString(encrypt);
  }


  @Nonnull
  public static String generateSecretTokenSalt(long timestamp) {
    String timestampStr = Long.toString(timestamp);
    String substring = timestampStr.substring(timestampStr.length() - 10);
    String reverse = StringUtils.reverse(substring);
    char[] chars = reverse.toCharArray();
    StringBuilder prefixBuilder = new StringBuilder();
    StringBuilder postfixBuilder = new StringBuilder();
    for (int i = 0; i < 6; i++) {
      if (i < 3) {
        prefixBuilder.append(chars[chars[i] - 48]);
      } else {
        postfixBuilder.append(chars[chars[i] - 48]);
      }
    }
    return prefixBuilder.append(reverse).append(postfixBuilder).toString();
  }

  @Nonnull
  public static String formatDate(long currentTimeMillis) {
    Instant instant = Instant.ofEpochMilli(currentTimeMillis);
    LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
    return dateTime.format(DateTimeFormatter.ISO_DATE_TIME);
  }

  public static long parseDate(@Nonnull String requestDateStr) {
    LocalDateTime dateTime = LocalDateTime.parse(requestDateStr, DateTimeFormatter.ISO_DATE_TIME);
    Instant instant = dateTime.toInstant(ZoneOffset.UTC);
    return instant.toEpochMilli();
  }

  private BasicAuthenticationUtils() {
  }
}
