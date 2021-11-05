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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

/**
 * @author 宋志宗 on 2021/4/27
 */
@SuppressWarnings({"DuplicatedCode", "unused"})
public final class Lists {

  public static boolean isEmpty(@Nullable List<?> list) {
    return list == null || list.isEmpty();
  }

  public static boolean isNotEmpty(@Nullable List<?> list) {
    return !Lists.isEmpty(list);
  }

  @Nonnull
  public static <E> List<E> of() {
    return Collections.emptyList();
  }

  @Nonnull
  public static <E> List<E> of(E e) {
    return Collections.singletonList(e);
  }

  @Nonnull
  public static <E> List<E> of(E e1, E e2) {
    List<E> list = new ArrayList<>(2);
    list.add(e1);
    list.add(e2);
    return Collections.unmodifiableList(list);
  }

  @Nonnull
  public static <E> List<E> of(E e1, E e2, E e3) {
    List<E> list = new ArrayList<>(3);
    list.add(e1);
    list.add(e2);
    list.add(e3);
    return Collections.unmodifiableList(list);
  }

  @Nonnull
  public static <E> List<E> of(E e1, E e2, E e3, E e4) {
    List<E> list = new ArrayList<>(4);
    list.add(e1);
    list.add(e2);
    list.add(e3);
    list.add(e4);
    return Collections.unmodifiableList(list);
  }

  @Nonnull
  public static <E> List<E> of(E e1, E e2, E e3, E e4, E e5) {
    List<E> list = new ArrayList<>(5);
    list.add(e1);
    list.add(e2);
    list.add(e3);
    list.add(e4);
    list.add(e5);
    return Collections.unmodifiableList(list);
  }

  @Nonnull
  public static <E> List<E> of(E e1, E e2, E e3, E e4, E e5, E e6) {
    List<E> list = new ArrayList<>(6);
    list.add(e1);
    list.add(e2);
    list.add(e3);
    list.add(e4);
    list.add(e5);
    list.add(e6);
    return Collections.unmodifiableList(list);
  }

  @Nonnull
  public static <E> List<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7) {
    List<E> list = new ArrayList<>(7);
    list.add(e1);
    list.add(e2);
    list.add(e3);
    list.add(e4);
    list.add(e5);
    list.add(e6);
    list.add(e7);
    return Collections.unmodifiableList(list);
  }

  @Nonnull
  public static <E> List<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8) {
    List<E> list = new ArrayList<>(8);
    list.add(e1);
    list.add(e2);
    list.add(e3);
    list.add(e4);
    list.add(e5);
    list.add(e6);
    list.add(e7);
    list.add(e8);
    return Collections.unmodifiableList(list);
  }

  @Nonnull
  @SafeVarargs
  public static <E> List<E> of(E... elements) {
    if (elements == null || elements.length == 0) {
      return of();
    }
    ArrayList<E> arrayList = new ArrayList<>(Arrays.asList(elements));
    return Collections.unmodifiableList(arrayList);
  }

  @Nonnull
  public static <E> ArrayList<E> arrayList(E e) {
    ArrayList<E> list = new ArrayList<>();
    list.add(e);
    return list;
  }

  @Nonnull
  public static <E> ArrayList<E> arrayList(E e1, E e2) {
    ArrayList<E> list = new ArrayList<>();
    list.add(e1);
    list.add(e2);
    return list;
  }

  @Nonnull
  public static <E> ArrayList<E> arrayList(E e1, E e2, E e3) {
    ArrayList<E> list = new ArrayList<>();
    list.add(e1);
    list.add(e2);
    list.add(e3);
    return list;
  }

  @Nonnull
  public static <E> ArrayList<E> arrayList(E e1, E e2, E e3, E e4) {
    ArrayList<E> list = new ArrayList<>();
    list.add(e1);
    list.add(e2);
    list.add(e3);
    list.add(e4);
    return list;
  }

  @Nonnull
  public static <E> ArrayList<E> arrayList(E e1, E e2, E e3, E e4, E e5) {
    ArrayList<E> list = new ArrayList<>();
    list.add(e1);
    list.add(e2);
    list.add(e3);
    list.add(e4);
    list.add(e5);
    return list;
  }

  @Nonnull
  public static <E> ArrayList<E> arrayList(E e1, E e2, E e3, E e4, E e5, E e6) {
    ArrayList<E> list = new ArrayList<>();
    list.add(e1);
    list.add(e2);
    list.add(e3);
    list.add(e4);
    list.add(e5);
    list.add(e6);
    return list;
  }

  @Nonnull
  public static <E> ArrayList<E> arrayList(E e1, E e2, E e3, E e4, E e5, E e6, E e7) {
    ArrayList<E> list = new ArrayList<>();
    list.add(e1);
    list.add(e2);
    list.add(e3);
    list.add(e4);
    list.add(e5);
    list.add(e6);
    list.add(e7);
    return list;
  }

  @Nonnull
  public static <E> ArrayList<E> arrayList(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8) {
    ArrayList<E> list = new ArrayList<>();
    list.add(e1);
    list.add(e2);
    list.add(e3);
    list.add(e4);
    list.add(e5);
    list.add(e6);
    list.add(e7);
    list.add(e8);
    return list;
  }

  @Nonnull
  @SafeVarargs
  public static <E> ArrayList<E> arrayList(E... elements) {
    if (elements == null || elements.length == 0) {
      return new ArrayList<>();
    }
    return new ArrayList<>(Arrays.asList(elements));
  }

  @Nonnull
  public static <E> List<E> merge(@Nonnull List<? extends E> list1,
                                  @Nonnull List<? extends E> list2) {
    int capacity = list1.size() + list2.size();
    List<E> result = new ArrayList<>(capacity);
    result.addAll(list1);
    result.addAll(list2);
    return result;
  }

  @Nonnull
  public static <E> List<E> merge(@Nonnull List<? extends E> list1,
                                  @Nonnull List<? extends E> list2,
                                  @Nonnull List<? extends E> list3) {
    int capacity = list1.size() + list2.size() + list3.size();
    List<E> result = new ArrayList<>(capacity);
    result.addAll(list1);
    result.addAll(list2);
    result.addAll(list3);
    return result;
  }

  @Nonnull
  public static <E> List<E> merge(@Nonnull List<? extends E> list1,
                                  @Nonnull List<? extends E> list2,
                                  @Nonnull List<? extends E> list3,
                                  @Nonnull List<? extends E> list4) {
    int capacity = list1.size() + list2.size() + list3.size() + list4.size();
    List<E> result = new ArrayList<>(capacity);
    result.addAll(list1);
    result.addAll(list2);
    result.addAll(list3);
    result.addAll(list4);
    return result;
  }

  @Nonnull
  @SafeVarargs
  public static <E> List<E> merge(@Nonnull List<? extends E> list,
                                  @Nonnull List<? extends E>... lists) {
    int capacity = list.size();
    for (List<? extends E> es : lists) {
      capacity += es.size();
    }
    List<E> result = new ArrayList<>(capacity);
    result.addAll(list);
    for (List<? extends E> ts : lists) {
      result.addAll(ts);
    }
    return result;
  }

  /**
   * List 去重
   *
   * @param list 输入对象
   * @param <E>  必须重写hashcode和equals方法
   * @return 去重后的结果
   * @author 宋志宗 on 2021/9/27
   */
  @Nonnull
  public static <E> List<E> distinct(@Nonnull List<E> list) {
    return new ArrayList<>(new LinkedHashSet<>(list));
  }

  /**
   * List去重
   *
   * @param list     需要去重的list
   * @param function 根据元素获取去重key
   * @return 去重后的list
   * @author 宋志宗 on 2021/9/27
   */
  @Nonnull
  public static <E, D> List<E> distinct(@Nonnull List<E> list, @Nonnull Function<E, D> function) {
    if (list.isEmpty()) {
      return list;
    }
    int initialCapacity = Math.max((int) (list.size() / 0.75F) + 1, 16);
    Map<D, Boolean> map = new HashMap<>(initialCapacity);
    List<E> result = new ArrayList<>();
    for (E e : list) {
      if (e == null) {
        continue;
      }
      D apply = function.apply(e);
      Boolean put = map.put(apply, Boolean.TRUE);
      if (put == null) {
        result.add(e);
      }
    }
    return result;
  }

  /**
   * 对集合进行分块
   *
   * @param list 集合
   * @param size 块大小
   * @author 宋志宗 on 2021/10/27
   */
  @Nonnull
  public static <E> List<List<E>> chunked(@Nonnull List<E> list, int size) {
    return CollectionUtils.chunked(list, size);
  }


  /**
   * 对集合进行分块并转换
   *
   * @param list      集合
   * @param size      块大小
   * @param transform 转换函数
   * @author 宋志宗 on 2021/10/27
   */
  @Nonnull
  public static <E, R> List<List<R>> chunked(@Nonnull List<E> list,
                                             int size, @Nonnull Function<E, R> transform) {
    return CollectionUtils.chunked(list, size, transform);
  }

  private Lists() {
  }
}
