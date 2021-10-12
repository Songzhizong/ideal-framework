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

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author 宋志宗 on 2021/9/19
 */
@SuppressWarnings("ConstantConditions")
public class ListsTest {

  @Test
  public void isEmpty() {
    assertTrue(Lists.isEmpty(null));
    assertTrue(Lists.isEmpty(new ArrayList<>()));
    assertFalse(Lists.isEmpty(new ArrayList<>(Arrays.asList(1, 2, 3))));
  }

  @Test
  public void isNotEmpty() {
    assertFalse(Lists.isNotEmpty(null));
    assertFalse(Lists.isNotEmpty(new ArrayList<>()));
    assertTrue(Lists.isNotEmpty(new ArrayList<>(Arrays.asList(1, 2, 3))));
  }

  @Test
  public void of() {
    List<Object> list = Lists.of();
    assertTrue(list != null && list.isEmpty());
    int f = 0;
    try {
      list.add(1);
    } catch (UnsupportedOperationException e) {
      f++;
    }
    assertEquals(1, f);
  }

  @Test
  public void testOf() {
    List<Integer> list = Lists.of(0);
    assertNotNull(list);
    assertEquals(1, list.size());
    assertEquals(0, (int) list.get(0));
    int f = 0;
    try {
      list.add(1);
    } catch (UnsupportedOperationException e) {
      f++;
    }
    assertEquals(1, f);
  }

  @Test
  public void testOf1() {
    List<Integer> list = Lists.of(0, 1);
    assertNotNull(list);
    assertEquals(2, list.size());
    assertEquals(0, (int) list.get(0));
    assertEquals(1, (int) list.get(1));
    int f = 0;
    try {
      list.add(1);
    } catch (UnsupportedOperationException e) {
      f++;
    }
    assertEquals(1, f);
  }

  @Test
  public void testOf2() {
    List<Integer> list = Lists.of(0, 1, 2);
    assertNotNull(list);
    assertEquals(3, list.size());
    assertEquals(0, (int) list.get(0));
    assertEquals(1, (int) list.get(1));
    assertEquals(2, (int) list.get(2));
    int f = 0;
    try {
      list.add(1);
    } catch (UnsupportedOperationException e) {
      f++;
    }
    assertEquals(1, f);
  }

  @Test
  public void testOf3() {
    List<Integer> list = Lists.of(0, 1, 2, 3);
    assertNotNull(list);
    assertEquals(4, list.size());
    assertEquals(0, (int) list.get(0));
    assertEquals(1, (int) list.get(1));
    assertEquals(2, (int) list.get(2));
    assertEquals(3, (int) list.get(3));
    int f = 0;
    try {
      list.add(1);
    } catch (UnsupportedOperationException e) {
      f++;
    }
    assertEquals(1, f);
  }

  @Test
  public void testOf4() {
    List<Integer> list = Lists.of(0, 1, 2, 3, 4);
    assertNotNull(list);
    assertEquals(5, list.size());
    assertEquals(0, (int) list.get(0));
    assertEquals(1, (int) list.get(1));
    assertEquals(2, (int) list.get(2));
    assertEquals(3, (int) list.get(3));
    assertEquals(4, (int) list.get(4));
    int f = 0;
    try {
      list.add(1);
    } catch (UnsupportedOperationException e) {
      f++;
    }
    assertEquals(1, f);
  }

  @Test
  public void testOf5() {
    List<Integer> list = Lists.of(0, 1, 2, 3, 4, 5);
    assertNotNull(list);
    assertEquals(6, list.size());
    assertEquals(0, (int) list.get(0));
    assertEquals(1, (int) list.get(1));
    assertEquals(2, (int) list.get(2));
    assertEquals(3, (int) list.get(3));
    assertEquals(4, (int) list.get(4));
    assertEquals(5, (int) list.get(5));
    int f = 0;
    try {
      list.add(1);
    } catch (UnsupportedOperationException e) {
      f++;
    }
    assertEquals(1, f);
  }

  @Test
  public void testOf6() {
    List<Integer> list = Lists.of(0, 1, 2, 3, 4, 5, 6);
    assertNotNull(list);
    assertEquals(7, list.size());
    assertEquals(0, (int) list.get(0));
    assertEquals(1, (int) list.get(1));
    assertEquals(2, (int) list.get(2));
    assertEquals(3, (int) list.get(3));
    assertEquals(4, (int) list.get(4));
    assertEquals(5, (int) list.get(5));
    assertEquals(6, (int) list.get(6));
    int f = 0;
    try {
      list.add(1);
    } catch (UnsupportedOperationException e) {
      f++;
    }
    assertEquals(1, f);
  }

  @Test
  public void testOf7() {
    List<Integer> list = Lists.of(0, 1, 2, 3, 4, 5, 6, 7);
    assertNotNull(list);
    assertEquals(8, list.size());
    assertEquals(0, (int) list.get(0));
    assertEquals(1, (int) list.get(1));
    assertEquals(2, (int) list.get(2));
    assertEquals(3, (int) list.get(3));
    assertEquals(4, (int) list.get(4));
    assertEquals(5, (int) list.get(5));
    assertEquals(6, (int) list.get(6));
    assertEquals(7, (int) list.get(7));
    int f = 0;
    try {
      list.add(1);
    } catch (UnsupportedOperationException e) {
      f++;
    }
    assertEquals(1, f);
  }

  @Test
  public void testOf8() {
    List<Integer> list = Lists.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0);
    assertNotNull(list);
    assertEquals(11, list.size());
    assertEquals(0, (int) list.get(0));
    assertEquals(1, (int) list.get(1));
    assertEquals(2, (int) list.get(2));
    assertEquals(3, (int) list.get(3));
    assertEquals(4, (int) list.get(4));
    assertEquals(5, (int) list.get(5));
    assertEquals(6, (int) list.get(6));
    assertEquals(7, (int) list.get(7));
    assertEquals(8, (int) list.get(8));
    assertEquals(9, (int) list.get(9));
    assertEquals(0, (int) list.get(0));
    int f = 0;
    try {
      list.add(1);
    } catch (UnsupportedOperationException e) {
      f++;
    }
    assertEquals(1, f);
  }

  @Test
  public void newArrayList() {
  }

  @Test
  public void arrayList() {
  }

  @Test
  public void testArrayList() {
  }

  @Test
  public void testArrayList1() {
  }

  @Test
  public void testArrayList2() {
  }

  @Test
  public void testArrayList3() {
  }

  @Test
  public void testArrayList4() {
  }

  @Test
  public void testArrayList5() {
  }

  @Test
  public void testArrayList6() {
  }

  @Test
  public void testArrayList7() {
  }

  @Test
  public void merge() {
  }

  @Test
  public void testMerge() {
  }

  @Test
  public void testMerge1() {
  }

  @Test
  public void testMerge2() {
  }

  @Test
  public void distinctBy() {
    List<Tuple<String, String>> list = Lists.of(
      Tuple.of("1", "2"),
      Tuple.of("1", "2"),
      Tuple.of("2", "2"),
      Tuple.of("3", "2"),
      Tuple.of("4", "2"),
      Tuple.of("5", "2"),
      Tuple.of("6", "2"),
      Tuple.of("7", "2")
    );
    List<Tuple<String, String>> distinct = Lists.distinct(list, Tuple::getFirst);
    assertEquals(distinct.size(), list.size() - 1);
  }
}
