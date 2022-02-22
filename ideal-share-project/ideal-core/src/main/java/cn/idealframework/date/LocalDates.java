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
package cn.idealframework.date;

import lombok.Setter;

import javax.annotation.Nonnull;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Locale;

/**
 * @author 宋志宗 on 2020/12/31
 */
public final class LocalDates {
  private LocalDates() {
  }

  @Setter
  private static ZoneOffset offset = ZoneOffset.of("+8");
  private static final Locale LOCALE = Locale.SIMPLIFIED_CHINESE;

  @Nonnull
  public static LocalDate parse(@Nonnull String localDateString,
                                @Nonnull String pattern) {
    return parse(localDateString, pattern, LOCALE);
  }

  @Nonnull
  public static LocalDate parse(@Nonnull String localDateString,
                                @Nonnull String pattern,
                                @Nonnull Locale locale) {
    return LocalDate.parse(localDateString, DateTimeFormatters.getFormatter(pattern, locale));
  }

  @Nonnull
  public static String format(@Nonnull LocalDate localDate,
                              @Nonnull String pattern) {
    return format(localDate, pattern, LOCALE);
  }

  @Nonnull
  public static String format(@Nonnull LocalDate localDate,
                              @Nonnull String pattern,
                              @Nonnull Locale locale) {
    return localDate.format(DateTimeFormatters.getFormatter(pattern, locale));
  }

}
