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

import org.junit.Test;

/**
 * @author 宋志宗 on 2021/6/23
 */
public class AESTest {
  private static final String k = "jhsuzkfijxhuzgrb";

  @Test
  public void encrypt() {
    String test = AES.encrypt("{\"ak\":\"accessKey\",\"st\":\"ckXIpi8wucBprt8Yd5lfpKgK1Wc=\"}", k);
    System.out.println(test);
    String decrypt = AES.decrypt(test, k);
    System.out.println(decrypt);
  }

  @Test
  public void decrypt() {
  }
}