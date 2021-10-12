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

import cn.idealframework.util.Asserts;

import javax.annotation.Nonnull;
import java.text.*;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 宋志宗 on 2021/8/12
 */
public class SafetyDateFormat extends DateFormat {
  private static final Map<String, ThreadLocal<SimpleDateFormat>> SDF_MAP = new ConcurrentHashMap<>();
  private static final long serialVersionUID = -8844590030841737511L;
  private final String pattern;

  public SafetyDateFormat(@Nonnull String pattern) {
    Asserts.notBlank(pattern, "pattern must be not blank");
    this.pattern = pattern;
    SDF_MAP.computeIfAbsent(pattern, k -> ThreadLocal.withInitial(() -> new SimpleDateFormat(k)));
  }

  private SimpleDateFormat getDateFormat() {
    ThreadLocal<SimpleDateFormat> threadLocal = SDF_MAP.get(pattern);
    return threadLocal.get();
  }

  @Override
  public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
    SimpleDateFormat simpleDateFormat = getDateFormat();
    return simpleDateFormat.format(date, toAppendTo, fieldPosition);
  }

  @Override
  public AttributedCharacterIterator formatToCharacterIterator(@Nonnull Object obj) {
    SimpleDateFormat simpleDateFormat = getDateFormat();
    return simpleDateFormat.formatToCharacterIterator(obj);
  }

  @Override
  public Date parse(String source, ParsePosition pos) {
    SimpleDateFormat simpleDateFormat = getDateFormat();
    return simpleDateFormat.parse(source, pos);
  }

  @SuppressWarnings("MethodDoesntCallSuperMethod")
  @Override
  public Object clone() {
    return this;
  }
}
