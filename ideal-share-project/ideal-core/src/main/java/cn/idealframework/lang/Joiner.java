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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * 连接器
 *
 * @author 宋志宗 on 2021/7/30
 */
public class Joiner {
  private final String separator;
  private final String prefix;
  private final String postfix;
  private final boolean skipNull;

  public Joiner(@Nonnull String separator,
                @Nullable String prefix,
                @Nullable String postfix,
                boolean skipNull) {
    Asserts.nonnull(separator, "separator must be not null");
    this.separator = separator;
    this.prefix = prefix;
    this.postfix = postfix;
    this.skipNull = skipNull;
  }

  @Nonnull
  public static JoinerBuilder builder(@Nonnull String separator) {
    return new JoinerBuilder(separator);
  }

  @Nonnull
  public static <T> String join(@Nonnull T[] ts, @Nonnull String separator) {
    List<T> ts1 = Arrays.asList(ts);
    return join(ts1, separator);
  }

  @Nonnull
  public static String join(@Nonnull Iterable<?> parts, @Nonnull String separator) {
    StringBuilder stringBuilder = new StringBuilder();
    Iterator<?> iterator = parts.iterator();
    return appendTo(stringBuilder, iterator, separator, null, null, false).toString();
  }

  @Nonnull
  public static String join(@Nonnull Iterable<?> parts,
                            @Nonnull String separator,
                            @Nullable String prefix,
                            @Nullable String postfix) {
    StringBuilder stringBuilder = new StringBuilder();
    Iterator<?> iterator = parts.iterator();
    return appendTo(stringBuilder, iterator, separator, prefix, postfix, false).toString();
  }


  @Nonnull
  public static <T> String joinSkipNull(@Nonnull T[] ts, @Nonnull String separator) {
    List<T> ts1 = Arrays.asList(ts);
    return joinSkipNull(ts1, separator);
  }

  @Nonnull
  public static String joinSkipNull(@Nonnull Iterable<?> parts, @Nonnull String separator) {
    StringBuilder stringBuilder = new StringBuilder();
    Iterator<?> iterator = parts.iterator();
    return appendTo(stringBuilder, iterator, separator, null, null, true).toString();
  }

  @Nonnull
  public static String joinSkipNull(@Nonnull Iterable<?> parts,
                                    @Nonnull String separator,
                                    @Nullable String prefix,
                                    @Nullable String postfix) {
    StringBuilder stringBuilder = new StringBuilder();
    Iterator<?> iterator = parts.iterator();
    return appendTo(stringBuilder, iterator, separator, prefix, postfix, true).toString();
  }

  @Nonnull
  private static StringBuilder appendTo(@Nonnull StringBuilder stringBuilder,
                                        @Nonnull Iterator<?> parts,
                                        @Nonnull String separator,
                                        @Nullable String prefix,
                                        @Nullable String postfix,
                                        boolean skipNull) {
    if (prefix != null) {
      stringBuilder.append(prefix);
    }
    if (parts.hasNext()) {
      CharSequence part = toString(parts.next());
      if (part == null) {
        if (!skipNull) {
          stringBuilder.append(separator);
        }
      } else {
        stringBuilder.append(part);
        stringBuilder.append(separator);
      }
    }
    while (parts.hasNext()) {
      CharSequence part = toString(parts.next());
      if (part == null) {
        if (!skipNull) {
          stringBuilder.append(separator);
        }
      } else {
        stringBuilder.append(part);
        stringBuilder.append(separator);
      }
    }
    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
    if (postfix != null) {
      stringBuilder.append(postfix);
    }
    return stringBuilder;
  }

  @Nonnull
  public final String join(@Nonnull Iterable<?> parts) {
    return join(parts.iterator());
  }

  @Nonnull
  public final String join(@Nonnull Iterator<?> parts) {
    return appendTo(new StringBuilder(), parts, separator, prefix, postfix, skipNull).toString();
  }

  @Nonnull
  public final String join(@Nonnull Object[] parts) {
    return join(Arrays.asList(parts));
  }

  private static CharSequence toString(Object object) {
    if (object == null) {
      return null;
    }
    return (object instanceof CharSequence) ? (CharSequence) object : object.toString();
  }

  public static class JoinerBuilder {
    private final String separator;
    private String prefix = null;
    private String postfix = null;
    private boolean skipNull = false;

    public JoinerBuilder(@Nonnull String separator) {
      this.separator = separator;
    }

    @Nonnull
    public JoinerBuilder prefix(String prefix) {
      this.prefix = prefix;
      return this;
    }

    @Nonnull
    public JoinerBuilder postfix(String postfix) {
      this.postfix = postfix;
      return this;
    }

    @Nonnull
    public JoinerBuilder skipNull() {
      this.skipNull = true;
      return this;
    }

    @Nonnull
    public Joiner build() {
      return new Joiner(separator, prefix, postfix, skipNull);
    }
  }
}
