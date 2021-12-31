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
package cn.idealframework.json;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 宋志宗 on 2021/5/28
 */
public class JsonUtils {
  /** 完整的时间格式 */
  public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";
  /** 日期格式 */
  public static final String DATE_PATTERN = "yyyy-MM-dd";
  /** 时间格式 */
  public static final String TIME_PATTERN = "HH:mm:ss.SSS";


  /**
   * 对象转json字符串
   *
   * @param value 对象值
   * @return json string
   * @author 宋志宗 on 2020/10/23
   */
  @Nonnull
  public static <T> String toJsonString(@Nonnull T value) {
    return JacksonUtils.toJsonString(value);
  }

  /**
   * 对象转格式化的json字符串
   *
   * @param value 对象值
   * @return pretty json string
   * @author 宋志宗 on 2020/10/23
   */
  @Nonnull
  public static <T> String toPrettyJsonString(@Nonnull T value) {
    return JacksonUtils.toPrettyJsonString(value);
  }

  /**
   * 对象转json字符串并忽略null值
   *
   * @param value 对象值
   * @return ignore null json string
   * @author 宋志宗 on 2020/10/23
   */
  @Nonnull
  public static <T> String toJsonStringIgnoreNull(@Nonnull T value) {
    return JacksonUtils.toJsonStringIgnoreNull(value);
  }

  /**
   * 对象转格式化的json字符串并忽略null值
   *
   * @param value 对象值
   * @return ignore null pretty json string
   * @author 宋志宗 on 2020/10/23
   */
  @Nonnull
  public static <T> String toPrettyJsonStringIgnoreNull(@Nonnull T value) {
    return JacksonUtils.toPrettyJsonStringIgnoreNull(value);
  }

  @Nonnull
  public static <T> T parse(@Nonnull String jsonString, @Nonnull Class<T> clazz) {
    return JacksonUtils.parse(jsonString, clazz);
  }

  @Nonnull
  public static <T> T parse(@Nonnull String jsonString,
                            @Nonnull Class<?> parametrized,
                            @Nonnull Class<?> parameterClass) {
    return JacksonUtils.parse(jsonString, parametrized, parameterClass);
  }

  @Nonnull
  public static <T> T parse(@Nonnull String jsonString,
                            @Nonnull Class<?> parametrized,
                            @Nonnull Class<?> parameterClass1,
                            @Nonnull Class<?> parameterClass2) {
    return JacksonUtils.parse(jsonString, parametrized, parameterClass1, parameterClass2);
  }

  @Nonnull
  public static <T> T parse(@Nonnull String jsonString, @Nonnull TypeReference<T> type) {
    return JacksonUtils.parse(jsonString, type);
  }

  @Nonnull
  public static <T> List<T> parseList(@Nonnull String jsonString, @Nonnull Class<T> clazz) {
    return JacksonUtils.parseList(jsonString, clazz);
  }

  @Nonnull
  public static <T> Set<T> parseSet(@Nonnull String jsonString, @Nonnull Class<T> clazz) {
    return JacksonUtils.parseSet(jsonString, clazz);
  }

  @Nonnull
  public static <K, V> Map<K, V> parseMap(@Nonnull String jsonString,
                                          @Nonnull Class<K> keyClass,
                                          @Nonnull Class<V> valueClass) {
    return JacksonUtils.parseMap(jsonString, keyClass, valueClass);
  }

  private JsonUtils() {
  }
}
