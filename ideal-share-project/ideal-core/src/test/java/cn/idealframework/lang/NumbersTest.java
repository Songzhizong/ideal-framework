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
package cn.idealframework.lang;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author 宋志宗 on 2021/12/29
 */
public class NumbersTest {

  @Test
  public void isNumber1() {
    String str = Long.MAX_VALUE + "";
    Assert.assertTrue(Numbers.isNumber(str));
    System.out.println(Long.parseLong(str));
  }

  @Test
  public void isNumber2() {
    String str = Long.MIN_VALUE + "";
    Assert.assertTrue(Numbers.isNumber(str));
    System.out.println(Long.parseLong(str));
  }

  @Test
  public void isNumber3() {
    String str = "0";
    Assert.assertTrue(Numbers.isNumber(str));
    System.out.println(Long.parseLong(str));
  }

  @Test
  public void isNumber4() {
    String str = "+0";
    Assert.assertTrue(Numbers.isNumber(str));
    System.out.println(Long.parseLong(str));
  }

  @Test
  public void isNumber5() {
    String str = "-0";
    Assert.assertTrue(Numbers.isNumber(str));
    System.out.println(Long.parseLong(str));
  }

  @Test
  public void isNumber6() {
    String str = "000" + Long.MAX_VALUE;
    Assert.assertTrue(Numbers.isNumber(str));
    System.out.println(Long.parseLong(str));
  }

  @Test
  public void isNumber7() {
    String str = "-000" + Long.MAX_VALUE;
    Assert.assertTrue(Numbers.isNumber(str));
    System.out.println(Long.parseLong(str));
  }

  @Test
  public void isNumber8() {
    String str = "+0000000000000000000000000000000000000000000000000000" + Long.MAX_VALUE;
    Assert.assertTrue(Numbers.isNumber(str));
    System.out.println(Long.parseLong(str));
  }

  @Test
  public void isNumber101() {
    String str = "+";
    Assert.assertFalse(Numbers.isNumber(str));
  }

  @Test
  public void isNumber102() {
    String str = "-";
    Assert.assertFalse(Numbers.isNumber(str));
  }

  @Test
  public void isNumber103() {
    // Long.MAX_VALUE = 9223372036854775807
    String str = "9223372036854775808";
    Assert.assertFalse(Numbers.isNumber(str));
  }
}
