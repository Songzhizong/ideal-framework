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

/**
 * @author 宋志宗 on 2021/8/5
 */
public class CharSequenceUtils {

  static int indexOf(final CharSequence cs, final CharSequence searchChar, final int start) {
    if (cs instanceof String) {
      return ((String) cs).indexOf(searchChar.toString(), start);
    } else if (cs instanceof StringBuilder) {
      return ((StringBuilder) cs).indexOf(searchChar.toString(), start);
    } else if (cs instanceof StringBuffer) {
      return ((StringBuffer) cs).indexOf(searchChar.toString(), start);
    }
    return cs.toString().indexOf(searchChar.toString(), start);
  }


  static boolean regionMatches(final CharSequence cs, final boolean ignoreCase, final int thisStart,
                               final CharSequence substring, final int start, final int length) {
    if (cs instanceof String && substring instanceof String) {
      return ((String) cs).regionMatches(ignoreCase, thisStart, (String) substring, start, length);
    }
    int index1 = thisStart;
    int index2 = start;
    int tmpLen = length;

    // Extract these first so we detect NPEs the same as the java.lang.String version
    final int srcLen = cs.length() - thisStart;
    final int otherLen = substring.length() - start;

    // Check for invalid parameters
    if (thisStart < 0 || start < 0 || length < 0) {
      return false;
    }

    // Check that the regions are long enough
    if (srcLen < length || otherLen < length) {
      return false;
    }

    while (tmpLen-- > 0) {
      final char c1 = cs.charAt(index1++);
      final char c2 = substring.charAt(index2++);

      if (c1 == c2) {
        continue;
      }

      if (!ignoreCase) {
        return false;
      }

      // The real same check as in String.regionMatches():
      final char u1 = Character.toUpperCase(c1);
      final char u2 = Character.toUpperCase(c2);
      if (u1 != u2 && Character.toLowerCase(u1) != Character.toLowerCase(u2)) {
        return false;
      }
    }

    return true;
  }
}
