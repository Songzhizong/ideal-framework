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
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @author 宋志宗 on 2019-05-14
 */
@SuppressWarnings("unused")
public class AES {

  /**
   * AES加密
   *
   * @param input  明文
   * @param secret 密钥 长度16|32
   * @return 密文
   */
  public static String encrypt(@Nonnull String input, @Nonnull String secret) {
    byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
    byte[] encrypt = encrypt(bytes, secret);
    return Base64.getEncoder().encodeToString(encrypt);
  }

  public static byte[] encrypt(byte[] input, @Nonnull String secret) {
    try {
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "AES");
      cipher.init(Cipher.ENCRYPT_MODE, keySpec);
      return cipher.doFinal(input);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException
      | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * aes解密
   *
   * @param ciphertext 密文
   * @param secret     密钥 长度16|32
   * @return 明文
   */
  @Nonnull
  public static String decrypt(@Nonnull String ciphertext, @Nonnull String secret) {
    byte[] decode = Base64.getDecoder().decode(ciphertext);
    byte[] decrypt = decrypt(decode, secret);
    return new String(decrypt, StandardCharsets.UTF_8);
  }

  public static byte[] decrypt(byte[] cipherBytes, @Nonnull String secret) {
    try {
      Cipher aesCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "AES");
      aesCipher.init(Cipher.DECRYPT_MODE, keySpec);
      return aesCipher.doFinal(cipherBytes);
    } catch (NoSuchAlgorithmException | BadPaddingException
      | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException e) {
      throw new RuntimeException(e);
    }
  }
}
