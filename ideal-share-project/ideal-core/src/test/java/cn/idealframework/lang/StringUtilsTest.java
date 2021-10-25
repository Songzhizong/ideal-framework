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
 * @author 宋志宗 on 2021/10/25
 */
public class StringUtilsTest {

  @Test
  public void testJoin() {
    String[] s = {null, "1", null, "3", "4", null};
    String join = StringUtils.join(s, ",");
    System.out.println(join);
    Assert.assertEquals(",1,,3,4,", join);
  }
}
