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
package cn.idealframework.crypto;

import javax.annotation.Nonnull;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * @author 宋志宗 on 2019/9/8
 */
public class HmacSHA1 {

  /**
   * 使用 HMAC-SHA1 签名方法对对encryptText进行签名
   *
   * @param encryptText 被签名的字符串
   * @param encryptKey  密钥
   */
  public static byte[] encode(@Nonnull String encryptText, @Nonnull String encryptKey) {
    try {
      SecretKey secretKey = new SecretKeySpec(encryptKey.getBytes(StandardCharsets.UTF_8), "HmacSHA1");
      Mac mac = Mac.getInstance("HmacSHA1");
      mac.init(secretKey);
      return mac.doFinal(encryptText.getBytes(StandardCharsets.UTF_8));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
