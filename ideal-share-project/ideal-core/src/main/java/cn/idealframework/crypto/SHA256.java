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
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author 宋志宗 on 2020/8/7
 */
@SuppressWarnings("unused")
public final class SHA256 {
  private static final char[] HEX_CHARS
    = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

  private SHA256() {
  }

  @Nonnull
  public static String encode(@Nonnull String string) {
    return encode(string.getBytes(StandardCharsets.UTF_8));
  }

  @Nonnull
  public static String encode(byte[] bytes) {
    return digestAsHexString(bytes);
  }


  // ----------------------------------------  private ~ ~ ~
  public static byte[] digest(byte[] bytes) {
    return getDigest().digest(bytes);
  }

  public static char[] digestAsHexChars(byte[] bytes) {
    byte[] digest = digest(bytes);
    return encodeHex(digest);
  }

  @Nonnull
  public static String digestAsHexString(byte[] bytes) {
    char[] hexDigest = digestAsHexChars(bytes);
    return String.valueOf(hexDigest);
  }

  @SuppressWarnings("DuplicatedCode")
  private static char[] encodeHex(byte[] data) {
    int l = data.length;
    char[] out = new char[l << 1];
    for (int i = 0, j = 0; i < l; i++) {
      out[j++] = HEX_CHARS[(0xF0 & data[i]) >>> 4];
      out[j++] = HEX_CHARS[0x0F & data[i]];
    }
    return out;
  }

  @Nonnull
  private static MessageDigest getDigest() {
    String sha256 = "SHA-256";
    try {
      return MessageDigest.getInstance(sha256);
    } catch (NoSuchAlgorithmException ex) {
      throw new IllegalStateException("Could not find MessageDigest with algorithm \"" + sha256 + "\"", ex);
    }
  }
}

