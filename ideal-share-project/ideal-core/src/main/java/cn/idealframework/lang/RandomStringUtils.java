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

import java.util.Random;

/**
 * @author 宋志宗 on 2021/8/5
 */
public class RandomStringUtils {
  private static final Random RANDOM = new Random();

  /**
   * 随机生成数字
   *
   * @param count 字符数量
   * @author 宋志宗 on 2021/8/5
   */
  public static String randomNumeric(final int count) {
    return random(count, false, true);
  }

  /**
   * 随机生成字母个数字
   *
   * @param count 字符数量
   * @author 宋志宗 on 2021/8/5
   */
  public static String randomAlphanumeric(final int count) {
    return random(count, true, true);
  }

  public static String random(final int count, final boolean letters, final boolean numbers) {
    return random(count, 0, 0, letters, numbers);
  }

  public static String random(final int count, final int start, final int end, final boolean letters, final boolean numbers) {
    return random(count, start, end, letters, numbers, null, RANDOM);
  }

  public static String random(int count, int start, int end, final boolean letters, final boolean numbers,
                              final char[] chars, final Random random) {
    if (count == 0) {
      return StringUtils.EMPTY;
    } else if (count < 0) {
      throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
    }
    if (chars != null && chars.length == 0) {
      throw new IllegalArgumentException("The chars array must not be empty");
    }

    if (start == 0 && end == 0) {
      if (chars != null) {
        end = chars.length;
      } else if (!letters && !numbers) {
        end = Character.MAX_CODE_POINT;
      } else {
        end = 'z' + 1;
        start = ' ';
      }
    } else if (end <= start) {
      throw new IllegalArgumentException("Parameter end (" + end + ") must be greater than start (" + start + ")");
    }

    final int zero_digit_ascii = 48;
    final int first_letter_ascii = 65;

    if (chars == null && (numbers && end <= zero_digit_ascii
        || letters && end <= first_letter_ascii)) {
      throw new IllegalArgumentException("Parameter end (" + end + ") must be greater then (" + zero_digit_ascii + ") for generating digits " +
          "or greater then (" + first_letter_ascii + ") for generating letters.");
    }

    final StringBuilder builder = new StringBuilder(count);
    final int gap = end - start;

    while (count-- != 0) {
      final int codePoint;
      if (chars == null) {
        codePoint = random.nextInt(gap) + start;

        switch (Character.getType(codePoint)) {
          case Character.UNASSIGNED:
          case Character.PRIVATE_USE:
          case Character.SURROGATE:
            count++;
            continue;
        }

      } else {
        codePoint = chars[random.nextInt(gap) + start];
      }

      final int numberOfChars = Character.charCount(codePoint);
      if (count == 0 && numberOfChars > 1) {
        count++;
        continue;
      }

      if (letters && Character.isLetter(codePoint)
          || numbers && Character.isDigit(codePoint)
          || !letters && !numbers) {
        builder.appendCodePoint(codePoint);

        if (numberOfChars == 2) {
          count--;
        }

      } else {
        count++;
      }
    }
    return builder.toString();
  }
}
