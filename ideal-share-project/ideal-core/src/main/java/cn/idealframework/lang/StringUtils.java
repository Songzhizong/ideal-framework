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

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 宋志宗 on 2021/8/5
 */
public class StringUtils {
  public static final String EMPTY = "";
  public static final int INDEX_NOT_FOUND = -1;

  public static boolean isBlank(@Nullable CharSequence cs) {
    int strLen;
    if (cs == null || (strLen = cs.length()) == 0) {
      return true;
    }
    for (int i = 0; i < strLen; i++) {
      if (!Character.isWhitespace(cs.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  public static boolean isNotBlank(@Nullable CharSequence cs) {
    return !isBlank(cs);
  }

  public static boolean isAnyBlank(final CharSequence... css) {
    if (ArrayUtils.isEmpty(css)) {
      return false;
    }
    for (final CharSequence cs : css) {
      if (isBlank(cs)) {
        return true;
      }
    }
    return false;
  }

  public static boolean isAllBlank(final CharSequence... css) {
    if (ArrayUtils.isEmpty(css)) {
      return true;
    }
    for (final CharSequence cs : css) {
      if (isNotBlank(cs)) {
        return false;
      }
    }
    return true;
  }

  public static boolean contains(final CharSequence seq, final CharSequence searchSeq) {
    if (seq == null || searchSeq == null) {
      return false;
    }
    return CharSequenceUtils.indexOf(seq, searchSeq, 0) >= 0;
  }

  public static String reverse(final String str) {
    if (str == null) {
      return null;
    }
    return new StringBuilder(str).reverse().toString();
  }

  public static String[] split(final String str) {
    return split(str, null, -1);
  }

  public static String[] split(final String str, final String separatorChars) {
    return splitWorker(str, separatorChars, -1);
  }

  public static String[] split(final String str, final String separatorChars, final int max) {
    return splitWorker(str, separatorChars, max);
  }

  private static String[] splitWorker(final String str, final String separatorChars, final int max) {
    // Performance tuned for 2.0 (JDK1.4)
    // Direct code is quicker than StringTokenizer.
    // Also, StringTokenizer uses isSpace() not isWhitespace()

    if (str == null) {
      return null;
    }
    final int len = str.length();
    if (len == 0) {
      return ArrayUtils.EMPTY_STRING_ARRAY;
    }
    final List<String> list = new ArrayList<>();
    int sizePlus1 = 1;
    int i = 0;
    int start = 0;
    boolean match = false;
    if (separatorChars == null) {
      // Null separator means use whitespace
      while (i < len) {
        if (Character.isWhitespace(str.charAt(i))) {
          if (match) {
            if (sizePlus1++ == max) {
              i = len;
            }
            list.add(str.substring(start, i));
            match = false;
          }
          start = ++i;
          continue;
        }
        match = true;
        i++;
      }
    } else if (separatorChars.length() == 1) {
      // Optimise 1 character case
      final char sep = separatorChars.charAt(0);
      while (i < len) {
        if (str.charAt(i) == sep) {
          if (match) {
            if (sizePlus1++ == max) {
              i = len;
            }
            list.add(str.substring(start, i));
            match = false;
          }
          start = ++i;
          continue;
        }
        match = true;
        i++;
      }
    } else {
      // standard case
      while (i < len) {
        if (separatorChars.indexOf(str.charAt(i)) >= 0) {
          if (match) {
            if (sizePlus1++ == max) {
              i = len;
            }
            list.add(str.substring(start, i));
            match = false;
          }
          start = ++i;
          continue;
        }
        match = true;
        i++;
      }
    }
    if (match) {
      list.add(str.substring(start, i));
    }
    return list.toArray(ArrayUtils.EMPTY_STRING_ARRAY);
  }

  public static String substring(final String str, int start) {
    if (str == null) {
      return null;
    }

    // handle negatives, which means last n characters
    if (start < 0) {
      start = str.length() + start; // remember start is negative
    }

    if (start < 0) {
      start = 0;
    }
    if (start > str.length()) {
      return EMPTY;
    }

    return str.substring(start);
  }

  public static String substring(final String str, int start, int end) {
    if (str == null) {
      return null;
    }

    // handle negatives
    if (end < 0) {
      end = str.length() + end; // remember end is negative
    }
    if (start < 0) {
      start = str.length() + start; // remember start is negative
    }

    // check length next
    if (end > str.length()) {
      end = str.length();
    }

    // if start is greater than end, return ""
    if (start > end) {
      return EMPTY;
    }

    if (start < 0) {
      start = 0;
    }
    if (end < 0) {
      end = 0;
    }

    return str.substring(start, end);
  }

  public static String replace(final String text, final String searchString, final String replacement) {
    return replace(text, searchString, replacement, -1);
  }


  public static String replace(final String text, final String searchString, final String replacement, final int max) {
    return replace(text, searchString, replacement, max, false);
  }

  private static String replace(final String text, String searchString, final String replacement, int max, final boolean ignoreCase) {
    if (isEmpty(text) || isEmpty(searchString) || replacement == null || max == 0) {
      return text;
    }
    if (ignoreCase) {
      searchString = searchString.toLowerCase();
    }
    int start = 0;
    int end = ignoreCase ? indexOfIgnoreCase(text, searchString, start) : indexOf(text, searchString, start);
    if (end == INDEX_NOT_FOUND) {
      return text;
    }
    final int replLength = searchString.length();
    int increase = Math.max(replacement.length() - replLength, 0);
    increase *= max < 0 ? 16 : Math.min(max, 64);
    final StringBuilder buf = new StringBuilder(text.length() + increase);
    while (end != INDEX_NOT_FOUND) {
      buf.append(text, start, end).append(replacement);
      start = end + replLength;
      if (--max == 0) {
        break;
      }
      end = ignoreCase ? indexOfIgnoreCase(text, searchString, start) : indexOf(text, searchString, start);
    }
    buf.append(text, start, text.length());
    return buf.toString();
  }

  public static int indexOf(final CharSequence seq, final CharSequence searchSeq, final int startPos) {
    if (seq == null || searchSeq == null) {
      return INDEX_NOT_FOUND;
    }
    return CharSequenceUtils.indexOf(seq, searchSeq, startPos);
  }

  public static int indexOfIgnoreCase(final CharSequence str, final CharSequence searchStr, int startPos) {
    if (str == null || searchStr == null) {
      return INDEX_NOT_FOUND;
    }
    if (startPos < 0) {
      startPos = 0;
    }
    final int endLimit = str.length() - searchStr.length() + 1;
    if (startPos > endLimit) {
      return INDEX_NOT_FOUND;
    }
    if (searchStr.length() == 0) {
      return startPos;
    }
    for (int i = startPos; i < endLimit; i++) {
      if (CharSequenceUtils.regionMatches(str, true, i, searchStr, 0, searchStr.length())) {
        return i;
      }
    }
    return INDEX_NOT_FOUND;
  }

  public static boolean isEmpty(final CharSequence cs) {
    return cs == null || cs.length() == 0;
  }

  public static boolean equalsIgnoreCase(final CharSequence cs1, final CharSequence cs2) {
    if (cs1 == cs2) {
      return true;
    }
    if (cs1 == null || cs2 == null) {
      return false;
    }
    if (cs1.length() != cs2.length()) {
      return false;
    }
    return CharSequenceUtils.regionMatches(cs1, true, 0, cs2, 0, cs1.length());
  }

  public static boolean notEqualsIgnoreCase(final CharSequence cs1, final CharSequence cs2) {
    return !equalsIgnoreCase(cs1, cs2);
  }
}
