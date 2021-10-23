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
import java.util.ArrayList;
import java.util.List;

/**
 * @author 宋志宗 on 2021/8/5
 */
public class StringUtils {
  public static final String EMPTY = "";
  public static final int INDEX_NOT_FOUND = -1;

  /**
   * <p>Checks if a CharSequence is empty (""), null or whitespace only.</p>
   *
   * <p>Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
   *
   * <pre>
   * StringUtils.isBlank(null)      = true
   * StringUtils.isBlank("")        = true
   * StringUtils.isBlank(" ")       = true
   * StringUtils.isBlank("bob")     = false
   * StringUtils.isBlank("  bob  ") = false
   * </pre>
   *
   * @param cs the CharSequence to check, may be null
   * @return {@code true} if the CharSequence is null, empty or whitespace only
   */
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

  /**
   * <p>Checks if a CharSequence is not empty (""), not null and not whitespace only.</p>
   *
   * <p>Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
   *
   * <pre>
   * StringUtils.isNotBlank(null)      = false
   * StringUtils.isNotBlank("")        = false
   * StringUtils.isNotBlank(" ")       = false
   * StringUtils.isNotBlank("bob")     = true
   * StringUtils.isNotBlank("  bob  ") = true
   * </pre>
   *
   * @param cs the CharSequence to check, may be null
   * @return {@code true} if the CharSequence is
   * not empty and not null and not whitespace only
   */
  public static boolean isNotBlank(@Nullable CharSequence cs) {
    return !isBlank(cs);
  }

  /**
   * <p>Checks if any of the CharSequences are empty ("") or null or whitespace only.</p>
   *
   * <p>Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
   *
   * <pre>
   * StringUtils.isAnyBlank((String) null)    = true
   * StringUtils.isAnyBlank((String[]) null)  = false
   * StringUtils.isAnyBlank(null, "foo")      = true
   * StringUtils.isAnyBlank(null, null)       = true
   * StringUtils.isAnyBlank("", "bar")        = true
   * StringUtils.isAnyBlank("bob", "")        = true
   * StringUtils.isAnyBlank("  bob  ", null)  = true
   * StringUtils.isAnyBlank(" ", "bar")       = true
   * StringUtils.isAnyBlank(new String[] {})  = false
   * StringUtils.isAnyBlank(new String[]{""}) = true
   * StringUtils.isAnyBlank("foo", "bar")     = false
   * </pre>
   *
   * @param css the CharSequences to check, may be null or empty
   * @return {@code true} if any of the CharSequences are empty or null or whitespace only
   */
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

  /**
   * <p>Checks if none of the CharSequences are empty (""), null or whitespace only.</p>
   *
   * <p>Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
   *
   * <pre>
   * StringUtils.isNoneBlank((String) null)    = false
   * StringUtils.isNoneBlank((String[]) null)  = true
   * StringUtils.isNoneBlank(null, "foo")      = false
   * StringUtils.isNoneBlank(null, null)       = false
   * StringUtils.isNoneBlank("", "bar")        = false
   * StringUtils.isNoneBlank("bob", "")        = false
   * StringUtils.isNoneBlank("  bob  ", null)  = false
   * StringUtils.isNoneBlank(" ", "bar")       = false
   * StringUtils.isNoneBlank(new String[] {})  = true
   * StringUtils.isNoneBlank(new String[]{""}) = false
   * StringUtils.isNoneBlank("foo", "bar")     = true
   * </pre>
   *
   * @param css the CharSequences to check, may be null or empty
   * @return {@code true} if none of the CharSequences are empty or null or whitespace only
   * @since 3.2
   */
  public static boolean isNoneBlank(final CharSequence... css) {
    return !isAnyBlank(css);
  }

  /**
   * <p>Checks if all of the CharSequences are empty (""), null or whitespace only.</p>
   *
   * <p>Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
   *
   * <pre>
   * StringUtils.isAllBlank(null)             = true
   * StringUtils.isAllBlank(null, "foo")      = false
   * StringUtils.isAllBlank(null, null)       = true
   * StringUtils.isAllBlank("", "bar")        = false
   * StringUtils.isAllBlank("bob", "")        = false
   * StringUtils.isAllBlank("  bob  ", null)  = false
   * StringUtils.isAllBlank(" ", "bar")       = false
   * StringUtils.isAllBlank("foo", "bar")     = false
   * StringUtils.isAllBlank(new String[] {})  = true
   * </pre>
   *
   * @param css the CharSequences to check, may be null or empty
   * @return {@code true} if all of the CharSequences are empty or null or whitespace only
   */
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

  /**
   * <p>Removes control characters (char &lt;= 32) from both
   * ends of this String, handling {@code null} by returning
   * {@code null}.</p>
   *
   * <p>The String is trimmed using {@link String#trim()}.
   * Trim removes start and end characters &lt;= 32.
   * To strip whitespace use {@link #strip(String)}.</p>
   *
   * <p>To trim your choice of characters, use the
   * {@link #strip(String, String)} methods.</p>
   *
   * <pre>
   * StringUtils.trim(null)          = null
   * StringUtils.trim("")            = ""
   * StringUtils.trim("     ")       = ""
   * StringUtils.trim("abc")         = "abc"
   * StringUtils.trim("    abc    ") = "abc"
   * </pre>
   *
   * @param str the String to be trimmed, may be null
   * @return the trimmed string, {@code null} if null String input
   */
  public static String trim(final String str) {
    return str == null ? null : str.trim();
  }

  /**
   * <p>Removes control characters (char &lt;= 32) from both
   * ends of this String returning {@code null} if the String is
   * empty ("") after the trim or if it is {@code null}.
   *
   * <p>The String is trimmed using {@link String#trim()}.
   * Trim removes start and end characters &lt;= 32.
   * To strip whitespace use {@link #stripToNull(String)}.</p>
   *
   * <pre>
   * StringUtils.trimToNull(null)          = null
   * StringUtils.trimToNull("")            = null
   * StringUtils.trimToNull("     ")       = null
   * StringUtils.trimToNull("abc")         = "abc"
   * StringUtils.trimToNull("    abc    ") = "abc"
   * </pre>
   *
   * @param str the String to be trimmed, may be null
   * @return the trimmed String,
   * {@code null} if only chars &lt;= 32, empty or null String input
   */
  @Nullable
  public static String trimToNull(final String str) {
    final String ts = trim(str);
    return isEmpty(ts) ? null : ts;
  }

  /**
   * <p>Removes control characters (char &lt;= 32) from both
   * ends of this String returning an empty String ("") if the String
   * is empty ("") after the trim or if it is {@code null}.
   *
   * <p>The String is trimmed using {@link String#trim()}.
   * Trim removes start and end characters &lt;= 32.
   * To strip whitespace use {@link #stripToEmpty(String)}.</p>
   *
   * <pre>
   * StringUtils.trimToEmpty(null)          = ""
   * StringUtils.trimToEmpty("")            = ""
   * StringUtils.trimToEmpty("     ")       = ""
   * StringUtils.trimToEmpty("abc")         = "abc"
   * StringUtils.trimToEmpty("    abc    ") = "abc"
   * </pre>
   *
   * @param str the String to be trimmed, may be null
   * @return the trimmed String, or an empty String if {@code null} input
   */
  @Nonnull
  public static String trimToEmpty(final String str) {
    return str == null ? EMPTY : str.trim();
  }

  /**
   * Truncates a String. This will turn "Now is the time for all good men" into "Now is the time for".
   * <pre>
   * StringUtils.truncate(null, 0)       = null
   * StringUtils.truncate(null, 2)       = null
   * StringUtils.truncate("", 4)         = ""
   * StringUtils.truncate("abcdefg", 4)  = "abcd"
   * StringUtils.truncate("abcdefg", 6)  = "abcdef"
   * StringUtils.truncate("abcdefg", 7)  = "abcdefg"
   * StringUtils.truncate("abcdefg", 8)  = "abcdefg"
   * StringUtils.truncate("abcdefg", -1) = throws an IllegalArgumentException
   * </pre>
   *
   * @param str      the String to truncate, may be null
   * @param maxWidth maximum length of result String, must be positive
   * @return truncated String, {@code null} if null String input
   */
  public static String truncate(final String str, final int maxWidth) {
    return truncate(str, 0, maxWidth);
  }

  /**
   * <p>Truncates a String. This will turn
   * "Now is the time for all good men" into "is the time for all".</p>
   *
   * <p>Works like {@code truncate(String, int)}, but allows you to specify
   * a "left edge" offset.
   *
   * <p>Specifically:</p>
   * <ul>
   *   <li>If {@code str} is less than {@code maxWidth} characters
   *       long, return it.</li>
   *   <li>Else truncate it to {@code substring(str, offset, maxWidth)}.</li>
   *   <li>If {@code maxWidth} is less than {@code 0}, throw an
   *       {@code IllegalArgumentException}.</li>
   *   <li>If {@code offset} is less than {@code 0}, throw an
   *       {@code IllegalArgumentException}.</li>
   *   <li>In no case will it return a String of length greater than
   *       {@code maxWidth}.</li>
   * </ul>
   *
   * <pre>
   * StringUtils.truncate(null, 0, 0) = null
   * StringUtils.truncate(null, 2, 4) = null
   * StringUtils.truncate("", 0, 10) = ""
   * StringUtils.truncate("", 2, 10) = ""
   * StringUtils.truncate("abcdefghij", 0, 3) = "abc"
   * StringUtils.truncate("abcdefghij", 5, 6) = "fghij"
   * StringUtils.truncate("raspberry peach", 10, 15) = "peach"
   * StringUtils.truncate("abcdefghijklmno", 0, 10) = "abcdefghij"
   * StringUtils.truncate("abcdefghijklmno", -1, 10) = throws an IllegalArgumentException
   * StringUtils.truncate("abcdefghijklmno", Integer.MIN_VALUE, 10) = "abcdefghij"
   * StringUtils.truncate("abcdefghijklmno", Integer.MIN_VALUE, Integer.MAX_VALUE) = "abcdefghijklmno"
   * StringUtils.truncate("abcdefghijklmno", 0, Integer.MAX_VALUE) = "abcdefghijklmno"
   * StringUtils.truncate("abcdefghijklmno", 1, 10) = "bcdefghijk"
   * StringUtils.truncate("abcdefghijklmno", 2, 10) = "cdefghijkl"
   * StringUtils.truncate("abcdefghijklmno", 3, 10) = "defghijklm"
   * StringUtils.truncate("abcdefghijklmno", 4, 10) = "efghijklmn"
   * StringUtils.truncate("abcdefghijklmno", 5, 10) = "fghijklmno"
   * StringUtils.truncate("abcdefghijklmno", 5, 5) = "fghij"
   * StringUtils.truncate("abcdefghijklmno", 5, 3) = "fgh"
   * StringUtils.truncate("abcdefghijklmno", 10, 3) = "klm"
   * StringUtils.truncate("abcdefghijklmno", 10, Integer.MAX_VALUE) = "klmno"
   * StringUtils.truncate("abcdefghijklmno", 13, 1) = "n"
   * StringUtils.truncate("abcdefghijklmno", 13, Integer.MAX_VALUE) = "no"
   * StringUtils.truncate("abcdefghijklmno", 14, 1) = "o"
   * StringUtils.truncate("abcdefghijklmno", 14, Integer.MAX_VALUE) = "o"
   * StringUtils.truncate("abcdefghijklmno", 15, 1) = ""
   * StringUtils.truncate("abcdefghijklmno", 15, Integer.MAX_VALUE) = ""
   * StringUtils.truncate("abcdefghijklmno", Integer.MAX_VALUE, Integer.MAX_VALUE) = ""
   * StringUtils.truncate("abcdefghij", 3, -1) = throws an IllegalArgumentException
   * StringUtils.truncate("abcdefghij", -2, 4) = throws an IllegalArgumentException
   * </pre>
   *
   * @param str      the String to check, may be null
   * @param offset   left edge of source String
   * @param maxWidth maximum length of result String, must be positive
   * @return truncated String, {@code null} if null String input
   */
  @Nullable
  public static String truncate(final String str, final int offset, final int maxWidth) {
    if (offset < 0) {
      throw new IllegalArgumentException("offset cannot be negative");
    }
    if (maxWidth < 0) {
      throw new IllegalArgumentException("maxWith cannot be negative");
    }
    if (str == null) {
      return null;
    }
    if (offset > str.length()) {
      return EMPTY;
    }
    if (str.length() > maxWidth) {
      final int ix = Math.min(offset + maxWidth, str.length());
      return str.substring(offset, ix);
    }
    return str.substring(offset);
  }

  /**
   * <p>Strips whitespace from the start and end of a String.</p>
   *
   * <p>This is similar to {@link #trim(String)} but removes whitespace.
   * Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
   *
   * <p>A {@code null} input String returns {@code null}.</p>
   *
   * <pre>
   * StringUtils.strip(null)     = null
   * StringUtils.strip("")       = ""
   * StringUtils.strip("   ")    = ""
   * StringUtils.strip("abc")    = "abc"
   * StringUtils.strip("  abc")  = "abc"
   * StringUtils.strip("abc  ")  = "abc"
   * StringUtils.strip(" abc ")  = "abc"
   * StringUtils.strip(" ab c ") = "ab c"
   * </pre>
   *
   * @param str the String to remove whitespace from, may be null
   * @return the stripped String, {@code null} if null String input
   */
  public static String strip(final String str) {
    return strip(str, null);
  }

  /**
   * <p>Strips whitespace from the start and end of a String  returning
   * {@code null} if the String is empty ("") after the strip.</p>
   *
   * <p>This is similar to {@link #trimToNull(String)} but removes whitespace.
   * Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
   *
   * <pre>
   * StringUtils.stripToNull(null)     = null
   * StringUtils.stripToNull("")       = null
   * StringUtils.stripToNull("   ")    = null
   * StringUtils.stripToNull("abc")    = "abc"
   * StringUtils.stripToNull("  abc")  = "abc"
   * StringUtils.stripToNull("abc  ")  = "abc"
   * StringUtils.stripToNull(" abc ")  = "abc"
   * StringUtils.stripToNull(" ab c ") = "ab c"
   * </pre>
   *
   * @param str the String to be stripped, may be null
   * @return the stripped String,
   * {@code null} if whitespace, empty or null String input
   */
  public static String stripToNull(String str) {
    if (str == null) {
      return null;
    }
    str = strip(str, null);
    return str.isEmpty() ? null : str;
  }

  /**
   * <p>Strips whitespace from the start and end of a String  returning
   * an empty String if {@code null} input.</p>
   *
   * <p>This is similar to {@link #trimToEmpty(String)} but removes whitespace.
   * Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
   *
   * <pre>
   * StringUtils.stripToEmpty(null)     = ""
   * StringUtils.stripToEmpty("")       = ""
   * StringUtils.stripToEmpty("   ")    = ""
   * StringUtils.stripToEmpty("abc")    = "abc"
   * StringUtils.stripToEmpty("  abc")  = "abc"
   * StringUtils.stripToEmpty("abc  ")  = "abc"
   * StringUtils.stripToEmpty(" abc ")  = "abc"
   * StringUtils.stripToEmpty(" ab c ") = "ab c"
   * </pre>
   *
   * @param str the String to be stripped, may be null
   * @return the trimmed String, or an empty String if {@code null} input
   */
  public static String stripToEmpty(final String str) {
    return str == null ? EMPTY : strip(str, null);
  }

  /**
   * <p>Strips any of a set of characters from the start and end of a String.
   * This is similar to {@link String#trim()} but allows the characters
   * to be stripped to be controlled.</p>
   *
   * <p>A {@code null} input String returns {@code null}.
   * An empty string ("") input returns the empty string.</p>
   *
   * <p>If the stripChars String is {@code null}, whitespace is
   * stripped as defined by {@link Character#isWhitespace(char)}.
   * Alternatively use {@link #strip(String)}.</p>
   *
   * <pre>
   * StringUtils.strip(null, *)          = null
   * StringUtils.strip("", *)            = ""
   * StringUtils.strip("abc", null)      = "abc"
   * StringUtils.strip("  abc", null)    = "abc"
   * StringUtils.strip("abc  ", null)    = "abc"
   * StringUtils.strip(" abc ", null)    = "abc"
   * StringUtils.strip("  abcyx", "xyz") = "  abc"
   * </pre>
   *
   * @param str        the String to remove characters from, may be null
   * @param stripChars the characters to remove, null treated as whitespace
   * @return the stripped String, {@code null} if null String input
   */
  public static String strip(String str, final String stripChars) {
    if (isEmpty(str)) {
      return str;
    }
    str = stripStart(str, stripChars);
    return stripEnd(str, stripChars);
  }

  /**
   * <p>Strips any of a set of characters from the end of a String.</p>
   *
   * <p>A {@code null} input String returns {@code null}.
   * An empty string ("") input returns the empty string.</p>
   *
   * <p>If the stripChars String is {@code null}, whitespace is
   * stripped as defined by {@link Character#isWhitespace(char)}.</p>
   *
   * <pre>
   * StringUtils.stripEnd(null, *)          = null
   * StringUtils.stripEnd("", *)            = ""
   * StringUtils.stripEnd("abc", "")        = "abc"
   * StringUtils.stripEnd("abc", null)      = "abc"
   * StringUtils.stripEnd("  abc", null)    = "  abc"
   * StringUtils.stripEnd("abc  ", null)    = "abc"
   * StringUtils.stripEnd(" abc ", null)    = " abc"
   * StringUtils.stripEnd("  abcyx", "xyz") = "  abc"
   * StringUtils.stripEnd("120.00", ".0")   = "12"
   * </pre>
   *
   * @param str        the String to remove characters from, may be null
   * @param stripChars the set of characters to remove, null treated as whitespace
   * @return the stripped String, {@code null} if null String input
   */
  public static String stripEnd(final String str, final String stripChars) {
    int end;
    if (str == null || (end = str.length()) == 0) {
      return str;
    }

    if (stripChars == null) {
      while (end != 0 && Character.isWhitespace(str.charAt(end - 1))) {
        end--;
      }
    } else if (stripChars.isEmpty()) {
      return str;
    } else {
      while (end != 0 && stripChars.indexOf(str.charAt(end - 1)) != INDEX_NOT_FOUND) {
        end--;
      }
    }
    return str.substring(0, end);
  }

  /**
   * <p>Strips any of a set of characters from the start of a String.</p>
   *
   * <p>A {@code null} input String returns {@code null}.
   * An empty string ("") input returns the empty string.</p>
   *
   * <p>If the stripChars String is {@code null}, whitespace is
   * stripped as defined by {@link Character#isWhitespace(char)}.</p>
   *
   * <pre>
   * StringUtils.stripStart(null, *)          = null
   * StringUtils.stripStart("", *)            = ""
   * StringUtils.stripStart("abc", "")        = "abc"
   * StringUtils.stripStart("abc", null)      = "abc"
   * StringUtils.stripStart("  abc", null)    = "abc"
   * StringUtils.stripStart("abc  ", null)    = "abc  "
   * StringUtils.stripStart(" abc ", null)    = "abc "
   * StringUtils.stripStart("yxabc  ", "xyz") = "abc  "
   * </pre>
   *
   * @param str        the String to remove characters from, may be null
   * @param stripChars the characters to remove, null treated as whitespace
   * @return the stripped String, {@code null} if null String input
   */
  public static String stripStart(final String str, final String stripChars) {
    int strLen;
    if (str == null || (strLen = str.length()) == 0) {
      return str;
    }
    int start = 0;
    if (stripChars == null) {
      while (start != strLen && Character.isWhitespace(str.charAt(start))) {
        start++;
      }
    } else if (stripChars.isEmpty()) {
      return str;
    } else {
      while (start != strLen && stripChars.indexOf(str.charAt(start)) != INDEX_NOT_FOUND) {
        start++;
      }
    }
    return str.substring(start);
  }

  /**
   * <p>Strips whitespace from the start and end of every String in an array.
   * Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
   *
   * <p>A new array is returned each time, except for length zero.
   * A {@code null} array will return {@code null}.
   * An empty array will return itself.
   * A {@code null} array entry will be ignored.</p>
   *
   * <pre>
   * StringUtils.stripAll(null)             = null
   * StringUtils.stripAll([])               = []
   * StringUtils.stripAll(["abc", "  abc"]) = ["abc", "abc"]
   * StringUtils.stripAll(["abc  ", null])  = ["abc", null]
   * </pre>
   *
   * @param strs the array to remove whitespace from, may be null
   * @return the stripped Strings, {@code null} if null array input
   */
  public static String[] stripAll(final String... strs) {
    return stripAll(strs, null);
  }

  /**
   * <p>Strips any of a set of characters from the start and end of every
   * String in an array.</p>
   * <p>Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
   *
   * <p>A new array is returned each time, except for length zero.
   * A {@code null} array will return {@code null}.
   * An empty array will return itself.
   * A {@code null} array entry will be ignored.
   * A {@code null} stripChars will strip whitespace as defined by
   * {@link Character#isWhitespace(char)}.</p>
   *
   * <pre>
   * StringUtils.stripAll(null, *)                = null
   * StringUtils.stripAll([], *)                  = []
   * StringUtils.stripAll(["abc", "  abc"], null) = ["abc", "abc"]
   * StringUtils.stripAll(["abc  ", null], null)  = ["abc", null]
   * StringUtils.stripAll(["abc  ", null], "yz")  = ["abc  ", null]
   * StringUtils.stripAll(["yabcz", null], "yz")  = ["abc", null]
   * </pre>
   *
   * @param strs       the array to remove characters from, may be null
   * @param stripChars the characters to remove, null treated as whitespace
   * @return the stripped Strings, {@code null} if null array input
   */
  public static String[] stripAll(final String[] strs, final String stripChars) {
    int strsLen;
    if (strs == null || (strsLen = strs.length) == 0) {
      return strs;
    }
    final String[] newArr = new String[strsLen];
    for (int i = 0; i < strsLen; i++) {
      newArr[i] = strip(strs[i], stripChars);
    }
    return newArr;
  }

  /**
   * Returns the index within <code>seq</code> of the first occurrence of
   * the specified character. If a character with value
   * <code>searchChar</code> occurs in the character sequence represented by
   * <code>seq</code> <code>CharSequence</code> object, then the index (in Unicode
   * code units) of the first such occurrence is returned. For
   * values of <code>searchChar</code> in the range from 0 to 0xFFFF
   * (inclusive), this is the smallest value <i>k</i> such that:
   * <blockquote><pre>
   * this.charAt(<i>k</i>) == searchChar
   * </pre></blockquote>
   * is true. For other values of <code>searchChar</code>, it is the
   * smallest value <i>k</i> such that:
   * <blockquote><pre>
   * this.codePointAt(<i>k</i>) == searchChar
   * </pre></blockquote>
   * is true. In either case, if no such character occurs in <code>seq</code>,
   * then {@code INDEX_NOT_FOUND (-1)} is returned.
   *
   * <p>Furthermore, a {@code null} or empty ("") CharSequence will
   * return {@code INDEX_NOT_FOUND (-1)}.</p>
   *
   * <pre>
   * StringUtils.indexOf(null, *)         = -1
   * StringUtils.indexOf("", *)           = -1
   * StringUtils.indexOf("aabaabaa", 'a') = 0
   * StringUtils.indexOf("aabaabaa", 'b') = 2
   * </pre>
   *
   * @param seq        the CharSequence to check, may be null
   * @param searchChar the character to find
   * @return the first index of the search character,
   * -1 if no match or {@code null} string input
   * @since 2.0
   * @since 3.0 Changed signature from indexOf(String, int) to indexOf(CharSequence, int)
   * @since 3.6 Updated {@link CharSequenceUtils} call to behave more like <code>String</code>
   */
  public static int indexOf(final CharSequence seq, final int searchChar) {
    if (isEmpty(seq)) {
      return INDEX_NOT_FOUND;
    }
    return CharSequenceUtils.indexOf(seq, searchChar, 0);
  }

  /**
   * Returns the index within <code>seq</code> of the first occurrence of the
   * specified character, starting the search at the specified index.
   * <p>
   * If a character with value <code>searchChar</code> occurs in the
   * character sequence represented by the <code>seq</code> <code>CharSequence</code>
   * object at an index no smaller than <code>startPos</code>, then
   * the index of the first such occurrence is returned. For values
   * of <code>searchChar</code> in the range from 0 to 0xFFFF (inclusive),
   * this is the smallest value <i>k</i> such that:
   * <blockquote><pre>
   * (this.charAt(<i>k</i>) == searchChar) &amp;&amp; (<i>k</i> &gt;= startPos)
   * </pre></blockquote>
   * is true. For other values of <code>searchChar</code>, it is the
   * smallest value <i>k</i> such that:
   * <blockquote><pre>
   * (this.codePointAt(<i>k</i>) == searchChar) &amp;&amp; (<i>k</i> &gt;= startPos)
   * </pre></blockquote>
   * is true. In either case, if no such character occurs in <code>seq</code>
   * at or after position <code>startPos</code>, then
   * <code>-1</code> is returned.
   *
   * <p>
   * There is no restriction on the value of <code>startPos</code>. If it
   * is negative, it has the same effect as if it were zero: this entire
   * string may be searched. If it is greater than the length of this
   * string, it has the same effect as if it were equal to the length of
   * this string: {@code (INDEX_NOT_FOUND) -1} is returned. Furthermore, a
   * {@code null} or empty ("") CharSequence will
   * return {@code (INDEX_NOT_FOUND) -1}.
   *
   * <p>All indices are specified in <code>char</code> values
   * (Unicode code units).
   *
   * <pre>
   * StringUtils.indexOf(null, *, *)          = -1
   * StringUtils.indexOf("", *, *)            = -1
   * StringUtils.indexOf("aabaabaa", 'b', 0)  = 2
   * StringUtils.indexOf("aabaabaa", 'b', 3)  = 5
   * StringUtils.indexOf("aabaabaa", 'b', 9)  = -1
   * StringUtils.indexOf("aabaabaa", 'b', -1) = 2
   * </pre>
   *
   * @param seq        the CharSequence to check, may be null
   * @param searchChar the character to find
   * @param startPos   the start position, negative treated as zero
   * @return the first index of the search character (always &ge; startPos),
   * -1 if no match or {@code null} string input
   * @since 2.0
   * @since 3.0 Changed signature from indexOf(String, int, int) to indexOf(CharSequence, int, int)
   * @since 3.6 Updated {@link CharSequenceUtils} call to behave more like <code>String</code>
   */
  public static int indexOf(final CharSequence seq, final int searchChar, final int startPos) {
    if (isEmpty(seq)) {
      return INDEX_NOT_FOUND;
    }
    return CharSequenceUtils.indexOf(seq, searchChar, startPos);
  }

  /**
   * <p>Finds the first index within a CharSequence, handling {@code null}.
   * This method uses {@link String#indexOf(String, int)} if possible.</p>
   *
   * <p>A {@code null} CharSequence will return {@code -1}.</p>
   *
   * <pre>
   * StringUtils.indexOf(null, *)          = -1
   * StringUtils.indexOf(*, null)          = -1
   * StringUtils.indexOf("", "")           = 0
   * StringUtils.indexOf("", *)            = -1 (except when * = "")
   * StringUtils.indexOf("aabaabaa", "a")  = 0
   * StringUtils.indexOf("aabaabaa", "b")  = 2
   * StringUtils.indexOf("aabaabaa", "ab") = 1
   * StringUtils.indexOf("aabaabaa", "")   = 0
   * </pre>
   *
   * @param seq       the CharSequence to check, may be null
   * @param searchSeq the CharSequence to find, may be null
   * @return the first index of the search CharSequence,
   * -1 if no match or {@code null} string input
   * @since 2.0
   * @since 3.0 Changed signature from indexOf(String, String) to indexOf(CharSequence, CharSequence)
   */
  public static int indexOf(final CharSequence seq, final CharSequence searchSeq) {
    if (seq == null || searchSeq == null) {
      return INDEX_NOT_FOUND;
    }
    return CharSequenceUtils.indexOf(seq, searchSeq, 0);
  }

  /**
   * <p>Finds the first index within a CharSequence, handling {@code null}.
   * This method uses {@link String#indexOf(String, int)} if possible.</p>
   *
   * <p>A {@code null} CharSequence will return {@code -1}.
   * A negative start position is treated as zero.
   * An empty ("") search CharSequence always matches.
   * A start position greater than the string length only matches
   * an empty search CharSequence.</p>
   *
   * <pre>
   * StringUtils.indexOf(null, *, *)          = -1
   * StringUtils.indexOf(*, null, *)          = -1
   * StringUtils.indexOf("", "", 0)           = 0
   * StringUtils.indexOf("", *, 0)            = -1 (except when * = "")
   * StringUtils.indexOf("aabaabaa", "a", 0)  = 0
   * StringUtils.indexOf("aabaabaa", "b", 0)  = 2
   * StringUtils.indexOf("aabaabaa", "ab", 0) = 1
   * StringUtils.indexOf("aabaabaa", "b", 3)  = 5
   * StringUtils.indexOf("aabaabaa", "b", 9)  = -1
   * StringUtils.indexOf("aabaabaa", "b", -1) = 2
   * StringUtils.indexOf("aabaabaa", "", 2)   = 2
   * StringUtils.indexOf("abc", "", 9)        = 3
   * </pre>
   *
   * @param seq       the CharSequence to check, may be null
   * @param searchSeq the CharSequence to find, may be null
   * @param startPos  the start position, negative treated as zero
   * @return the first index of the search CharSequence (always &ge; startPos),
   * -1 if no match or {@code null} string input
   * @since 2.0
   * @since 3.0 Changed signature from indexOf(String, String, int) to indexOf(CharSequence, CharSequence, int)
   */
  public static int indexOf(final CharSequence seq, final CharSequence searchSeq, final int startPos) {
    if (seq == null || searchSeq == null) {
      return INDEX_NOT_FOUND;
    }
    return CharSequenceUtils.indexOf(seq, searchSeq, startPos);
  }

  /**
   * <p>Finds the n-th index within a CharSequence, handling {@code null}.
   * This method uses {@link String#indexOf(String)} if possible.</p>
   * <p><b>Note:</b> The code starts looking for a match at the start of the target,
   * incrementing the starting index by one after each successful match
   * (unless {@code searchStr} is an empty string in which case the position
   * is never incremented and {@code 0} is returned immediately).
   * This means that matches may overlap.</p>
   * <p>A {@code null} CharSequence will return {@code -1}.</p>
   *
   * <pre>
   * StringUtils.ordinalIndexOf(null, *, *)          = -1
   * StringUtils.ordinalIndexOf(*, null, *)          = -1
   * StringUtils.ordinalIndexOf("", "", *)           = 0
   * StringUtils.ordinalIndexOf("aabaabaa", "a", 1)  = 0
   * StringUtils.ordinalIndexOf("aabaabaa", "a", 2)  = 1
   * StringUtils.ordinalIndexOf("aabaabaa", "b", 1)  = 2
   * StringUtils.ordinalIndexOf("aabaabaa", "b", 2)  = 5
   * StringUtils.ordinalIndexOf("aabaabaa", "ab", 1) = 1
   * StringUtils.ordinalIndexOf("aabaabaa", "ab", 2) = 4
   * StringUtils.ordinalIndexOf("aabaabaa", "", 1)   = 0
   * StringUtils.ordinalIndexOf("aabaabaa", "", 2)   = 0
   * </pre>
   *
   * <p>Matches may overlap:</p>
   * <pre>
   * StringUtils.ordinalIndexOf("ababab","aba", 1)   = 0
   * StringUtils.ordinalIndexOf("ababab","aba", 2)   = 2
   * StringUtils.ordinalIndexOf("ababab","aba", 3)   = -1
   *
   * StringUtils.ordinalIndexOf("abababab", "abab", 1) = 0
   * StringUtils.ordinalIndexOf("abababab", "abab", 2) = 2
   * StringUtils.ordinalIndexOf("abababab", "abab", 3) = 4
   * StringUtils.ordinalIndexOf("abababab", "abab", 4) = -1
   * </pre>
   *
   * <p>Note that 'head(CharSequence str, int n)' may be implemented as: </p>
   *
   * <pre>
   *   str.substring(0, lastOrdinalIndexOf(str, "\n", n))
   * </pre>
   *
   * @param str       the CharSequence to check, may be null
   * @param searchStr the CharSequence to find, may be null
   * @param ordinal   the n-th {@code searchStr} to find
   * @return the n-th index of the search CharSequence,
   * {@code -1} ({@code INDEX_NOT_FOUND}) if no match or {@code null} string input
   * @since 2.1
   * @since 3.0 Changed signature from ordinalIndexOf(String, String, int) to ordinalIndexOf(CharSequence, CharSequence, int)
   */
  public static int ordinalIndexOf(final CharSequence str, final CharSequence searchStr, final int ordinal) {
    return ordinalIndexOf(str, searchStr, ordinal, false);
  }

  /**
   * <p>Finds the n-th index within a String, handling {@code null}.
   * This method uses {@link String#indexOf(String)} if possible.</p>
   * <p>Note that matches may overlap<p>
   *
   * <p>A {@code null} CharSequence will return {@code -1}.</p>
   *
   * @param str       the CharSequence to check, may be null
   * @param searchStr the CharSequence to find, may be null
   * @param ordinal   the n-th {@code searchStr} to find, overlapping matches are allowed.
   * @param lastIndex true if lastOrdinalIndexOf() otherwise false if ordinalIndexOf()
   * @return the n-th index of the search CharSequence,
   * {@code -1} ({@code INDEX_NOT_FOUND}) if no match or {@code null} string input
   */
  // Shared code between ordinalIndexOf(String,String,int) and lastOrdinalIndexOf(String,String,int)
  private static int ordinalIndexOf(final CharSequence str, final CharSequence searchStr, final int ordinal, final boolean lastIndex) {
    if (str == null || searchStr == null || ordinal <= 0) {
      return INDEX_NOT_FOUND;
    }
    if (searchStr.length() == 0) {
      return lastIndex ? str.length() : 0;
    }
    int found = 0;
    // set the initial index beyond the end of the string
    // this is to allow for the initial index decrement/increment
    int index = lastIndex ? str.length() : INDEX_NOT_FOUND;
    do {
      if (lastIndex) {
        index = CharSequenceUtils.lastIndexOf(str, searchStr, index - 1); // step backwards thru string
      } else {
        index = CharSequenceUtils.indexOf(str, searchStr, index + 1); // step forwards through string
      }
      if (index < 0) {
        return index;
      }
      found++;
    } while (found < ordinal);
    return index;
  }

  /**
   * <p>Case in-sensitive find of the first index within a CharSequence.</p>
   *
   * <p>A {@code null} CharSequence will return {@code -1}.
   * A negative start position is treated as zero.
   * An empty ("") search CharSequence always matches.
   * A start position greater than the string length only matches
   * an empty search CharSequence.</p>
   *
   * <pre>
   * StringUtils.indexOfIgnoreCase(null, *)          = -1
   * StringUtils.indexOfIgnoreCase(*, null)          = -1
   * StringUtils.indexOfIgnoreCase("", "")           = 0
   * StringUtils.indexOfIgnoreCase("aabaabaa", "a")  = 0
   * StringUtils.indexOfIgnoreCase("aabaabaa", "b")  = 2
   * StringUtils.indexOfIgnoreCase("aabaabaa", "ab") = 1
   * </pre>
   *
   * @param str       the CharSequence to check, may be null
   * @param searchStr the CharSequence to find, may be null
   * @return the first index of the search CharSequence,
   * -1 if no match or {@code null} string input
   * @since 2.5
   * @since 3.0 Changed signature from indexOfIgnoreCase(String, String) to indexOfIgnoreCase(CharSequence, CharSequence)
   */
  public static int indexOfIgnoreCase(final CharSequence str, final CharSequence searchStr) {
    return indexOfIgnoreCase(str, searchStr, 0);
  }

  /**
   * <p>Case in-sensitive find of the first index within a CharSequence
   * from the specified position.</p>
   *
   * <p>A {@code null} CharSequence will return {@code -1}.
   * A negative start position is treated as zero.
   * An empty ("") search CharSequence always matches.
   * A start position greater than the string length only matches
   * an empty search CharSequence.</p>
   *
   * <pre>
   * StringUtils.indexOfIgnoreCase(null, *, *)          = -1
   * StringUtils.indexOfIgnoreCase(*, null, *)          = -1
   * StringUtils.indexOfIgnoreCase("", "", 0)           = 0
   * StringUtils.indexOfIgnoreCase("aabaabaa", "A", 0)  = 0
   * StringUtils.indexOfIgnoreCase("aabaabaa", "B", 0)  = 2
   * StringUtils.indexOfIgnoreCase("aabaabaa", "AB", 0) = 1
   * StringUtils.indexOfIgnoreCase("aabaabaa", "B", 3)  = 5
   * StringUtils.indexOfIgnoreCase("aabaabaa", "B", 9)  = -1
   * StringUtils.indexOfIgnoreCase("aabaabaa", "B", -1) = 2
   * StringUtils.indexOfIgnoreCase("aabaabaa", "", 2)   = 2
   * StringUtils.indexOfIgnoreCase("abc", "", 9)        = -1
   * </pre>
   *
   * @param str       the CharSequence to check, may be null
   * @param searchStr the CharSequence to find, may be null
   * @param startPos  the start position, negative treated as zero
   * @return the first index of the search CharSequence (always &ge; startPos),
   * -1 if no match or {@code null} string input
   * @since 2.5
   * @since 3.0 Changed signature from indexOfIgnoreCase(String, String, int) to indexOfIgnoreCase(CharSequence, CharSequence, int)
   */
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

  /**
   * Returns the index within <code>seq</code> of the last occurrence of
   * the specified character. For values of <code>searchChar</code> in the
   * range from 0 to 0xFFFF (inclusive), the index (in Unicode code
   * units) returned is the largest value <i>k</i> such that:
   * <blockquote><pre>
   * this.charAt(<i>k</i>) == searchChar
   * </pre></blockquote>
   * is true. For other values of <code>searchChar</code>, it is the
   * largest value <i>k</i> such that:
   * <blockquote><pre>
   * this.codePointAt(<i>k</i>) == searchChar
   * </pre></blockquote>
   * is true.  In either case, if no such character occurs in this
   * string, then <code>-1</code> is returned. Furthermore, a {@code null} or empty ("")
   * <code>CharSequence</code> will return {@code -1}. The
   * <code>seq</code> <code>CharSequence</code> object is searched backwards
   * starting at the last character.
   *
   * <pre>
   * StringUtils.lastIndexOf(null, *)         = -1
   * StringUtils.lastIndexOf("", *)           = -1
   * StringUtils.lastIndexOf("aabaabaa", 'a') = 7
   * StringUtils.lastIndexOf("aabaabaa", 'b') = 5
   * </pre>
   *
   * @param seq        the <code>CharSequence</code> to check, may be null
   * @param searchChar the character to find
   * @return the last index of the search character,
   * -1 if no match or {@code null} string input
   * @since 2.0
   * @since 3.0 Changed signature from lastIndexOf(String, int) to lastIndexOf(CharSequence, int)
   * @since 3.6 Updated {@link CharSequenceUtils} call to behave more like <code>String</code>
   */
  public static int lastIndexOf(final CharSequence seq, final int searchChar) {
    if (isEmpty(seq)) {
      return INDEX_NOT_FOUND;
    }
    return CharSequenceUtils.lastIndexOf(seq, searchChar, seq.length());
  }

  /**
   * Returns the index within <code>seq</code> of the last occurrence of
   * the specified character, searching backward starting at the
   * specified index. For values of <code>searchChar</code> in the range
   * from 0 to 0xFFFF (inclusive), the index returned is the largest
   * value <i>k</i> such that:
   * <blockquote><pre>
   * (this.charAt(<i>k</i>) == searchChar) &amp;&amp; (<i>k</i> &lt;= startPos)
   * </pre></blockquote>
   * is true. For other values of <code>searchChar</code>, it is the
   * largest value <i>k</i> such that:
   * <blockquote><pre>
   * (this.codePointAt(<i>k</i>) == searchChar) &amp;&amp; (<i>k</i> &lt;= startPos)
   * </pre></blockquote>
   * is true. In either case, if no such character occurs in <code>seq</code>
   * at or before position <code>startPos</code>, then
   * <code>-1</code> is returned. Furthermore, a {@code null} or empty ("")
   * <code>CharSequence</code> will return {@code -1}. A start position greater
   * than the string length searches the whole string.
   * The search starts at the <code>startPos</code> and works backwards;
   * matches starting after the start position are ignored.
   *
   * <p>All indices are specified in <code>char</code> values
   * (Unicode code units).
   *
   * <pre>
   * StringUtils.lastIndexOf(null, *, *)          = -1
   * StringUtils.lastIndexOf("", *,  *)           = -1
   * StringUtils.lastIndexOf("aabaabaa", 'b', 8)  = 5
   * StringUtils.lastIndexOf("aabaabaa", 'b', 4)  = 2
   * StringUtils.lastIndexOf("aabaabaa", 'b', 0)  = -1
   * StringUtils.lastIndexOf("aabaabaa", 'b', 9)  = 5
   * StringUtils.lastIndexOf("aabaabaa", 'b', -1) = -1
   * StringUtils.lastIndexOf("aabaabaa", 'a', 0)  = 0
   * </pre>
   *
   * @param seq        the CharSequence to check, may be null
   * @param searchChar the character to find
   * @param startPos   the start position
   * @return the last index of the search character (always &le; startPos),
   * -1 if no match or {@code null} string input
   * @since 2.0
   * @since 3.0 Changed signature from lastIndexOf(String, int, int) to lastIndexOf(CharSequence, int, int)
   */
  public static int lastIndexOf(final CharSequence seq, final int searchChar, final int startPos) {
    if (isEmpty(seq)) {
      return INDEX_NOT_FOUND;
    }
    return CharSequenceUtils.lastIndexOf(seq, searchChar, startPos);
  }

  /**
   * <p>Finds the last index within a CharSequence, handling {@code null}.
   * This method uses {@link String#lastIndexOf(String)} if possible.</p>
   *
   * <p>A {@code null} CharSequence will return {@code -1}.</p>
   *
   * <pre>
   * StringUtils.lastIndexOf(null, *)          = -1
   * StringUtils.lastIndexOf(*, null)          = -1
   * StringUtils.lastIndexOf("", "")           = 0
   * StringUtils.lastIndexOf("aabaabaa", "a")  = 7
   * StringUtils.lastIndexOf("aabaabaa", "b")  = 5
   * StringUtils.lastIndexOf("aabaabaa", "ab") = 4
   * StringUtils.lastIndexOf("aabaabaa", "")   = 8
   * </pre>
   *
   * @param seq       the CharSequence to check, may be null
   * @param searchSeq the CharSequence to find, may be null
   * @return the last index of the search String,
   * -1 if no match or {@code null} string input
   * @since 2.0
   * @since 3.0 Changed signature from lastIndexOf(String, String) to lastIndexOf(CharSequence, CharSequence)
   */
  public static int lastIndexOf(final CharSequence seq, final CharSequence searchSeq) {
    if (seq == null || searchSeq == null) {
      return INDEX_NOT_FOUND;
    }
    return CharSequenceUtils.lastIndexOf(seq, searchSeq, seq.length());
  }

  /**
   * <p>Finds the n-th last index within a String, handling {@code null}.
   * This method uses {@link String#lastIndexOf(String)}.</p>
   *
   * <p>A {@code null} String will return {@code -1}.</p>
   *
   * <pre>
   * StringUtils.lastOrdinalIndexOf(null, *, *)          = -1
   * StringUtils.lastOrdinalIndexOf(*, null, *)          = -1
   * StringUtils.lastOrdinalIndexOf("", "", *)           = 0
   * StringUtils.lastOrdinalIndexOf("aabaabaa", "a", 1)  = 7
   * StringUtils.lastOrdinalIndexOf("aabaabaa", "a", 2)  = 6
   * StringUtils.lastOrdinalIndexOf("aabaabaa", "b", 1)  = 5
   * StringUtils.lastOrdinalIndexOf("aabaabaa", "b", 2)  = 2
   * StringUtils.lastOrdinalIndexOf("aabaabaa", "ab", 1) = 4
   * StringUtils.lastOrdinalIndexOf("aabaabaa", "ab", 2) = 1
   * StringUtils.lastOrdinalIndexOf("aabaabaa", "", 1)   = 8
   * StringUtils.lastOrdinalIndexOf("aabaabaa", "", 2)   = 8
   * </pre>
   *
   * <p>Note that 'tail(CharSequence str, int n)' may be implemented as: </p>
   *
   * <pre>
   *   str.substring(lastOrdinalIndexOf(str, "\n", n) + 1)
   * </pre>
   *
   * @param str       the CharSequence to check, may be null
   * @param searchStr the CharSequence to find, may be null
   * @param ordinal   the n-th last {@code searchStr} to find
   * @return the n-th last index of the search CharSequence,
   * {@code -1} ({@code INDEX_NOT_FOUND}) if no match or {@code null} string input
   * @since 2.5
   * @since 3.0 Changed signature from lastOrdinalIndexOf(String, String, int) to lastOrdinalIndexOf(CharSequence, CharSequence, int)
   */
  public static int lastOrdinalIndexOf(final CharSequence str, final CharSequence searchStr, final int ordinal) {
    return ordinalIndexOf(str, searchStr, ordinal, true);
  }

  /**
   * <p>Finds the last index within a CharSequence, handling {@code null}.
   * This method uses {@link String#lastIndexOf(String, int)} if possible.</p>
   *
   * <p>A {@code null} CharSequence will return {@code -1}.
   * A negative start position returns {@code -1}.
   * An empty ("") search CharSequence always matches unless the start position is negative.
   * A start position greater than the string length searches the whole string.
   * The search starts at the startPos and works backwards; matches starting after the start
   * position are ignored.
   * </p>
   *
   * <pre>
   * StringUtils.lastIndexOf(null, *, *)          = -1
   * StringUtils.lastIndexOf(*, null, *)          = -1
   * StringUtils.lastIndexOf("aabaabaa", "a", 8)  = 7
   * StringUtils.lastIndexOf("aabaabaa", "b", 8)  = 5
   * StringUtils.lastIndexOf("aabaabaa", "ab", 8) = 4
   * StringUtils.lastIndexOf("aabaabaa", "b", 9)  = 5
   * StringUtils.lastIndexOf("aabaabaa", "b", -1) = -1
   * StringUtils.lastIndexOf("aabaabaa", "a", 0)  = 0
   * StringUtils.lastIndexOf("aabaabaa", "b", 0)  = -1
   * StringUtils.lastIndexOf("aabaabaa", "b", 1)  = -1
   * StringUtils.lastIndexOf("aabaabaa", "b", 2)  = 2
   * StringUtils.lastIndexOf("aabaabaa", "ba", 2)  = 2
   * </pre>
   *
   * @param seq       the CharSequence to check, may be null
   * @param searchSeq the CharSequence to find, may be null
   * @param startPos  the start position, negative treated as zero
   * @return the last index of the search CharSequence (always &le; startPos),
   * -1 if no match or {@code null} string input
   * @since 2.0
   * @since 3.0 Changed signature from lastIndexOf(String, String, int) to lastIndexOf(CharSequence, CharSequence, int)
   */
  public static int lastIndexOf(final CharSequence seq, final CharSequence searchSeq, final int startPos) {
    if (seq == null || searchSeq == null) {
      return INDEX_NOT_FOUND;
    }
    return CharSequenceUtils.lastIndexOf(seq, searchSeq, startPos);
  }

  /**
   * <p>Case in-sensitive find of the last index within a CharSequence.</p>
   *
   * <p>A {@code null} CharSequence will return {@code -1}.
   * A negative start position returns {@code -1}.
   * An empty ("") search CharSequence always matches unless the start position is negative.
   * A start position greater than the string length searches the whole string.</p>
   *
   * <pre>
   * StringUtils.lastIndexOfIgnoreCase(null, *)          = -1
   * StringUtils.lastIndexOfIgnoreCase(*, null)          = -1
   * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "A")  = 7
   * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "B")  = 5
   * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "AB") = 4
   * </pre>
   *
   * @param str       the CharSequence to check, may be null
   * @param searchStr the CharSequence to find, may be null
   * @return the first index of the search CharSequence,
   * -1 if no match or {@code null} string input
   * @since 2.5
   * @since 3.0 Changed signature from lastIndexOfIgnoreCase(String, String) to lastIndexOfIgnoreCase(CharSequence, CharSequence)
   */
  public static int lastIndexOfIgnoreCase(final CharSequence str, final CharSequence searchStr) {
    if (str == null || searchStr == null) {
      return INDEX_NOT_FOUND;
    }
    return lastIndexOfIgnoreCase(str, searchStr, str.length());
  }

  /**
   * <p>Case in-sensitive find of the last index within a CharSequence
   * from the specified position.</p>
   *
   * <p>A {@code null} CharSequence will return {@code -1}.
   * A negative start position returns {@code -1}.
   * An empty ("") search CharSequence always matches unless the start position is negative.
   * A start position greater than the string length searches the whole string.
   * The search starts at the startPos and works backwards; matches starting after the start
   * position are ignored.
   * </p>
   *
   * <pre>
   * StringUtils.lastIndexOfIgnoreCase(null, *, *)          = -1
   * StringUtils.lastIndexOfIgnoreCase(*, null, *)          = -1
   * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "A", 8)  = 7
   * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "B", 8)  = 5
   * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "AB", 8) = 4
   * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "B", 9)  = 5
   * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "B", -1) = -1
   * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "A", 0)  = 0
   * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "B", 0)  = -1
   * </pre>
   *
   * @param str       the CharSequence to check, may be null
   * @param searchStr the CharSequence to find, may be null
   * @param startPos  the start position
   * @return the last index of the search CharSequence (always &le; startPos),
   * -1 if no match or {@code null} input
   * @since 2.5
   * @since 3.0 Changed signature from lastIndexOfIgnoreCase(String, String, int) to lastIndexOfIgnoreCase(CharSequence, CharSequence, int)
   */
  public static int lastIndexOfIgnoreCase(final CharSequence str, final CharSequence searchStr, int startPos) {
    if (str == null || searchStr == null) {
      return INDEX_NOT_FOUND;
    }
    if (startPos > str.length() - searchStr.length()) {
      startPos = str.length() - searchStr.length();
    }
    if (startPos < 0) {
      return INDEX_NOT_FOUND;
    }
    if (searchStr.length() == 0) {
      return startPos;
    }

    for (int i = startPos; i >= 0; i--) {
      if (CharSequenceUtils.regionMatches(str, true, i, searchStr, 0, searchStr.length())) {
        return i;
      }
    }
    return INDEX_NOT_FOUND;
  }

  /**
   * Gets a CharSequence length or {@code 0} if the CharSequence is
   * {@code null}.
   *
   * @param cs a CharSequence or {@code null}
   * @return CharSequence length or {@code 0} if the CharSequence is
   * {@code null}.
   */
  public static int length(final CharSequence cs) {
    return cs == null ? 0 : cs.length();
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
