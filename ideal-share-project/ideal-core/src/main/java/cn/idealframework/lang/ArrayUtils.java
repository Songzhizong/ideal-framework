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

import java.lang.reflect.Array;

/**
 * @author 宋志宗 on 2021/8/5
 */
public class ArrayUtils {
  public static final int INDEX_NOT_FOUND = -1;
  public static final String[] EMPTY_STRING_ARRAY = new String[0];

  public static boolean isEmpty(final Object[] array) {
    return getLength(array) == 0;
  }

  public static int getLength(final Object array) {
    if (array == null) {
      return 0;
    }
    return Array.getLength(array);
  }

  public static boolean contains(final int[] array, final int valueToFind) {
    return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
  }

  public static int indexOf(final int[] array, final int valueToFind) {
    return indexOf(array, valueToFind, 0);
  }

  public static int indexOf(final int[] array, final int valueToFind, int startIndex) {
    if (array == null) {
      return INDEX_NOT_FOUND;
    }
    if (startIndex < 0) {
      startIndex = 0;
    }
    for (int i = startIndex; i < array.length; i++) {
      if (valueToFind == array[i]) {
        return i;
      }
    }
    return INDEX_NOT_FOUND;
  }

  public static int indexOf(final long[] array, final long valueToFind) {
    return indexOf(array, valueToFind, 0);
  }

  public static int indexOf(final long[] array, final long valueToFind, int startIndex) {
    if (array == null) {
      return INDEX_NOT_FOUND;
    }
    if (startIndex < 0) {
      startIndex = 0;
    }
    for (int i = startIndex; i < array.length; i++) {
      if (valueToFind == array[i]) {
        return i;
      }
    }
    return INDEX_NOT_FOUND;
  }
}
