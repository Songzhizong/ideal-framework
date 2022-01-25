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

import cn.idealframework.json.JsonUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author 宋志宗 on 2022/1/25
 */
@SuppressWarnings({"ConstantConditions", "SpellCheckingInspection"})
public class CollectionUtilsTest {

  @Test
  public void isEmpty() {
    assertTrue(CollectionUtils.isEmpty(null));
    assertTrue(CollectionUtils.isEmpty(new ArrayList<>()));
    assertFalse(CollectionUtils.isEmpty(new ArrayList<>(Arrays.asList(1, 2, 3))));
  }

  @Test
  public void isNotEmpty() {
    assertFalse(Lists.isNotEmpty(null));
    assertFalse(Lists.isNotEmpty(new ArrayList<>()));
    assertTrue(Lists.isNotEmpty(new ArrayList<>(Arrays.asList(1, 2, 3))));
  }

  @Test
  public void chunked() {
  }

  @Test
  public void testChunked() {
  }

  @Test
  public void chunkedLimit() {
    List<Integer> olist1 = Lists.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
    List<List<Integer>> lists1_1 = CollectionUtils.chunkedLimit(olist1, 10);
    System.out.println(JsonUtils.toJsonString(lists1_1));
    assertEquals(9, lists1_1.size());
    List<List<Integer>> lists1_2 = CollectionUtils.chunkedLimit(olist1, 6);
    System.out.println(JsonUtils.toJsonString(lists1_2));
    assertEquals(6, lists1_2.size());

    List<Integer> olist2 = Lists.of(1);
    List<List<Integer>> lists2_1 = CollectionUtils.chunkedLimit(olist2, 5);
    System.out.println(JsonUtils.toJsonString(lists2_1));
    assertEquals(1, lists2_1.size());

  }
}
