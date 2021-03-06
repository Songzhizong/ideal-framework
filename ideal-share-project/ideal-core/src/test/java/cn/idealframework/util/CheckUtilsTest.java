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
package cn.idealframework.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author 宋志宗 on 2021/7/3
 */
public class CheckUtilsTest {

  @Test
  public void checkMobile() {
    boolean c1 = CheckUtils.checkMobile("18256928780");
    Assert.assertTrue(c1);
    boolean c2 = CheckUtils.checkMobile("1825692878");
    Assert.assertFalse(c2);
    boolean c3 = CheckUtils.checkMobile("1234567890");
    Assert.assertFalse(c3);
    boolean c4 = CheckUtils.checkMobile("19530258334");
    Assert.assertTrue(c4);
    boolean c5 = CheckUtils.checkMobile("29530258334");
    Assert.assertFalse(c5);
    boolean c6 = CheckUtils.checkMobile("195302583342");
    Assert.assertFalse(c6);
  }

  @Test
  public void checkAccount() {
    boolean c1 = CheckUtils.checkAccount("zzsong91");
    Assert.assertTrue(c1);
    boolean c2 = CheckUtils.checkAccount("1zzsong");
    Assert.assertFalse(c2);
    boolean c3 = CheckUtils.checkAccount("zzs");
    Assert.assertFalse(c3);
    boolean c4 = CheckUtils.checkAccount("zzsong");
    Assert.assertTrue(c4);
  }

  @Test
  public void checkEmail() {
    boolean c1 = CheckUtils.checkEmail("zzsong91@163.com");
    Assert.assertTrue(c1);
    boolean c2 = CheckUtils.checkEmail("zzsong91163.com");
    Assert.assertFalse(c2);
  }

  @Test
  public void checkIdCard() {
    boolean c1 = CheckUtils.checkIdCard("432012199305183238");
    Assert.assertTrue(c1);
    boolean c2 = CheckUtils.checkIdCard("43201219930518323");
    Assert.assertFalse(c2);
  }
}
