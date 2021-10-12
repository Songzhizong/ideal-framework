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

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author 宋志宗 on 2021/6/4
 */
public class NumberSystemConverterTest {

  @Test
  public void tenSystemTo() {

  }

  @Test
  public void toTenSystem() {
    String t1 = "1111111111111111111111";
    long l1 = NumberSystemConverter.toTenSystem(t1, 2);
    assertEquals(l1, 4194303L);
  }

}