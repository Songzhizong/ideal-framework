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

import java.util.List;

/**
 * @author 宋志宗 on 2021/7/30
 */
public class JoinerTest {

  @Test
  public void test() {

    Joiner skipNull = Joiner.builder(", ").prefix("{").postfix("}").skipNull().build();
    Joiner joiner = Joiner.builder(", ").prefix("{").postfix("}").build();

    List<String> stringList = Lists.of(null, "1", "2", "3", null, "5", null, "7");
    String join1 = skipNull.join(stringList);
    Assert.assertEquals("{1, 2, 3, 5, 7}", join1);

    String join2 = joiner.join(stringList);
    Assert.assertEquals("{, 1, 2, 3, , 5, , 7}", join2);


    List<Integer> intList = Lists.of(null, 1, 2, 3, null, 5, null);
    String join3 = skipNull.join(intList);
    Assert.assertEquals("{1, 2, 3, 5}", join3);

    String join4 = joiner.join(intList);
    Assert.assertEquals("{, 1, 2, 3, , 5, }", join4);
  }
}
