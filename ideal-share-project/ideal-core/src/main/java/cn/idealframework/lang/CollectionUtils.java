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

import cn.idealframework.util.Asserts;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * @author 宋志宗 on 2021/7/9
 */
@SuppressWarnings("DuplicatedCode")
public final class CollectionUtils {

  public static boolean isEmpty(@Nullable Collection<?> collection) {
    return collection == null || collection.isEmpty();
  }

  public static boolean isNotEmpty(@Nullable Collection<?> collection) {
    return !isEmpty(collection);
  }

  @Nonnull
  public static <E> List<List<E>> chunked(@Nullable Collection<E> collection, int size) {
    Asserts.assertTrue(size > 0, "The size must be greater than 0");
    if (CollectionUtils.isEmpty(collection)) {
      return new ArrayList<>();
    }
    int listSize = collection.size();
    int resultCapacity = listSize / size + ((listSize % size == 0) ? 0 : 1);
    List<List<E>> result = new ArrayList<>(resultCapacity);
    List<E> currentList = new ArrayList<>(size);
    int currentSize = size;
    for (E next : collection) {
      if (currentSize == size) {
        currentList = new ArrayList<>(size);
        result.add(currentList);
        currentSize = 0;
      }
      currentList.add(next);
      currentSize++;
    }
    return result;
  }

  @Nonnull
  public static <E, R> List<List<R>> chunked(@Nullable Collection<E> collection,
                                             int size, @Nonnull Function<E, R> transform) {
    Asserts.assertTrue(size > 0, "The size must be greater than 0");
    if (CollectionUtils.isEmpty(collection)) {
      return new ArrayList<>();
    }
    int listSize = collection.size();
    int resultCapacity = listSize / size + ((listSize % size == 0) ? 0 : 1);
    List<List<R>> result = new ArrayList<>(resultCapacity);
    List<R> currentList = new ArrayList<>(size);
    int currentSize = size;
    for (E next : collection) {
      if (currentSize == size) {
        currentList = new ArrayList<>(size);
        result.add(currentList);
        currentSize = 0;
      }
      currentList.add(transform.apply(next));
      currentSize++;
    }
    return result;
  }

  private CollectionUtils() {
  }
}
