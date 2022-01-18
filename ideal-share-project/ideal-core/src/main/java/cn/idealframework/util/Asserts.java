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

import cn.idealframework.lang.StringUtils;
import cn.idealframework.transmission.exception.BadRequestException;
import lombok.extern.apachecommons.CommonsLog;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author 宋志宗 on 2021/4/20
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
@CommonsLog
public final class Asserts {

  @Nonnull
  public static CharSequence notBlank(@Nullable CharSequence charSequence,
                                      @Nullable String message) throws IllegalArgumentException {
    if (StringUtils.isBlank(charSequence)) {
      throw genAssertException(message);
    }
    return charSequence;
  }

  @Nonnull
  public static CharSequence notBlank(@Nullable CharSequence charSequence,
                                      @Nonnull Supplier<String> messageSupplier) throws IllegalArgumentException {
    if (StringUtils.isBlank(charSequence)) {
      String message = messageSupplier.get();
      throw genAssertException(message);
    }
    return charSequence;
  }

  @Nonnull
  public static CharSequence ifBlankThrow(@Nullable CharSequence charSequence,
                                          @Nonnull Supplier<? extends RuntimeException> supplier) {
    if (StringUtils.isBlank(charSequence)) {
      throw supplier.get();
    }
    return charSequence;
  }

  @Nonnull
  public static <T> T nonnull(@Nullable T t, @Nullable String message) {
    if (t == null) {
      throw genAssertException(message);
    }
    return t;
  }

  @Nonnull
  public static <T> T nonnull(@Nullable T t, @Nonnull Supplier<String> messageSupplier) {
    if (t == null) {
      String message = messageSupplier.get();
      throw genAssertException(message);
    }
    return t;
  }

  @Nonnull
  public static <T> T ifNullThrow(@Nullable T t,
                                  @Nonnull Supplier<? extends RuntimeException> supplier) {
    if (t == null) {
      throw supplier.get();
    }
    return t;
  }

  public static void equals(@Nullable Object o1, @Nullable Object o2, @Nullable String message) {
    if (!Objects.equals(o1, o2)) {
      throw genAssertException(message);
    }
  }

  public static void equals(long l1, long l2, @Nullable String message) {
    if (l1 != l2) {
      throw genAssertException(message);
    }
  }

  public static void equals(int i1, int i2, @Nullable String message) {
    if (i1 != i2) {
      throw genAssertException(message);
    }
  }

  public static void notEquals(@Nullable Object o1, @Nullable Object o2, @Nullable String message) {
    if (Objects.equals(o1, o2)) {
      throw genAssertException(message);
    }
  }

  public static void notEquals(long l1, long l2, @Nullable String message) {
    if (l1 == l2) {
      throw genAssertException(message);
    }
  }

  public static void notEquals(int i1, int i2, @Nullable String message) {
    if (i1 == i2) {
      throw genAssertException(message);
    }
  }


  public static void maxLength(@Nonnull CharSequence charSequence, int length, @Nullable String message) {
    if (charSequence.length() > length) {
      throw genAssertException(message);
    }
  }

  public static void assertTrue(boolean expression, @Nullable String message) {
    if (!expression) {
      throw genAssertException(message);
    }
  }

  public static void assertTrue(boolean expression, @Nonnull Supplier<String> messageSupplier) {
    if (!expression) {
      throw genAssertException(messageSupplier.get());
    }
  }

  public static void assertFalse(boolean expression, @Nullable String message) {
    if (expression) {
      throw genAssertException(message);
    }
  }

  public static void assertFalse(boolean expression, @Nonnull Supplier<String> messageSupplier) {
    if (expression) {
      throw genAssertException(messageSupplier.get());
    }
  }

  @Nonnull
  public static <C extends Collection<?>> C notEmpty(@Nullable C collection,
                                                     @Nullable String message) {
    if (collection == null || collection.isEmpty()) {
      throw genAssertException(message);
    }
    return collection;
  }

  @Nonnull
  public static <C extends Collection<?>> C notEmpty(@Nullable C collection,
                                                     @Nonnull Supplier<String> messageSupplier) {
    if (collection == null || collection.isEmpty()) {
      throw genAssertException(messageSupplier.get());
    }
    return collection;
  }

  @Nonnull
  public static <K, V> Map<K, V> notEmpty(@Nullable Map<K, V> map,
                                          @Nullable String message) {
    if (map == null || map.isEmpty()) {
      throw genAssertException(message);
    }
    return map;
  }


  public static void range(long value, long minimum, long maximum,
                           @Nullable String message) throws IllegalArgumentException {
    if (value < minimum || value > maximum) {
      throw genAssertException(message);
    }
  }

  private static void printLog(@Nullable String message) {
    StackTraceElement[] stackTrace = new Throwable().getStackTrace();
    if (stackTrace.length > 3) {
      StackTraceElement stackTraceElement = stackTrace[3];
      String className = stackTraceElement.getClassName();
      int lineNumber = stackTraceElement.getLineNumber();
      log.info(className + " " + lineNumber + " : " + message);
    }
  }

  @Nonnull
  private static AssertException genAssertException(@Nullable String message) {
    if (message == null) {
      return new AssertException();
    }
    printLog(message);
    return new AssertException(message);
  }

  public static class AssertException extends BadRequestException {

    public AssertException(@Nonnull String message) {
      super(message);
    }

    public AssertException() {
      super("");
    }
  }

  private Asserts() {
  }
}
