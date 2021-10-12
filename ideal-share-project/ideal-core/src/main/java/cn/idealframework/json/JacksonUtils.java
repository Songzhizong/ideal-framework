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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import javax.annotation.Nonnull;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * JSON工具类
 * 依赖jackson
 *
 * @author 宋志宗 on 2020/10/23
 */
@SuppressWarnings({"unused", "SameParameterValue"})
public final class JacksonUtils {
  private JacksonUtils() {
  }

  private static final SimpleModule JAVA_TIME_MODULE = new JavaTimeModule();
  private static final ObjectMapper MAPPER = new ObjectMapper();
  private static final ObjectMapper IGNORE_NULL_MAPPER = new ObjectMapper();

  static {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(JsonUtils.DATE_TIME_PATTERN);
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(JsonUtils.DATE_PATTERN);
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(JsonUtils.TIME_PATTERN);
    JAVA_TIME_MODULE
      .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter))
      .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter))
      .addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter))
      .addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter))
      .addSerializer(LocalTime.class, new LocalTimeSerializer(timeFormatter))
      .addDeserializer(LocalTime.class, new LocalTimeDeserializer(timeFormatter));
    MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      .registerModule(JAVA_TIME_MODULE)
      .findAndRegisterModules();
    IGNORE_NULL_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      .setSerializationInclusion(JsonInclude.Include.NON_NULL)
      .registerModule(JAVA_TIME_MODULE)
      .findAndRegisterModules();
  }

  /**
   * 对象转json字符串
   *
   * @param value 对象值
   * @return json string
   * @author 宋志宗 on 2020/10/23
   */
  @Nonnull
  static <T> String toJsonString(@Nonnull T value) {
    return toJsonString(value, false, false);
  }


  /**
   * 对象转格式化的json字符串
   *
   * @param value 对象值
   * @return pretty json string
   * @author 宋志宗 on 2020/10/23
   */
  @Nonnull
  static <T> String toPrettyJsonString(@Nonnull T value) {
    return toJsonString(value, false, true);
  }

  /**
   * 对象转json字符串并忽略null值
   *
   * @param value 对象值
   * @return ignore null json string
   * @author 宋志宗 on 2020/10/23
   */
  @Nonnull
  static <T> String toJsonStringIgnoreNull(@Nonnull T value) {
    return toJsonString(value, true, false);
  }

  /**
   * 对象转格式化的json字符串并忽略null值
   *
   * @param value 对象值
   * @return ignore null pretty json string
   * @author 宋志宗 on 2020/10/23
   */
  @Nonnull
  static <T> String toPrettyJsonStringIgnoreNull(@Nonnull T value) {
    return toJsonString(value, true, true);
  }

  /**
   * 对象转json字符串
   *
   * @param value      对象值
   * @param ignoreNull 是否忽略null值
   * @param pretty     是否格式化
   * @return json string
   */
  @Nonnull
  static <T> String toJsonString(@Nonnull T value, boolean ignoreNull, boolean pretty) {
    ObjectMapper mapper = MAPPER;
    if (ignoreNull) {
      mapper = IGNORE_NULL_MAPPER;
    }
    try {
      if (pretty) {
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
      } else {
        return mapper.writeValueAsString(value);
      }
    } catch (JsonProcessingException e) {
      throw new JsonFormatException(e);
    }
  }


  @Nonnull
  static <T> T parse(@Nonnull String jsonString, @Nonnull Class<T> clazz) {
    try {
      return IGNORE_NULL_MAPPER.readValue(jsonString, clazz);
    } catch (JsonProcessingException e) {
      throw new JsonParseException(e);
    }
  }

  @Nonnull
  static <T> T parse(@Nonnull String jsonString,
                     @Nonnull Class<?> parametrized,
                     @Nonnull Class<?> parameterClass) {
    JavaType javaType = IGNORE_NULL_MAPPER.getTypeFactory()
      .constructParametricType(parametrized, parameterClass);
    try {
      return IGNORE_NULL_MAPPER.readValue(jsonString, javaType);
    } catch (JsonProcessingException e) {
      throw new JsonParseException(e);
    }
  }

  @Nonnull
  static <T> T parse(@Nonnull String jsonString,
                     @Nonnull Class<?> parametrized,
                     @Nonnull Class<?> parameterClass1,
                     @Nonnull Class<?> parameterClass2) {
    TypeFactory typeFactory = IGNORE_NULL_MAPPER.getTypeFactory();
    JavaType javaType = typeFactory.constructParametricType(parameterClass1, parameterClass2);
    JavaType type = typeFactory.constructParametricType(parametrized, javaType);
    try {
      return IGNORE_NULL_MAPPER.readValue(jsonString, type);
    } catch (JsonProcessingException e) {
      throw new JsonParseException(e);
    }
  }

  @Nonnull
  static <T> T parse(@Nonnull String jsonString,
                     @Nonnull Class<?> parametrized,
                     @Nonnull Class<?> parameterClass1,
                     @Nonnull Class<?> parameterClass2,
                     @Nonnull Class<?> parameterClass3) {
    TypeFactory typeFactory = IGNORE_NULL_MAPPER.getTypeFactory();
    JavaType javaType = typeFactory.constructParametricType(parameterClass2, parameterClass3);
    JavaType type = typeFactory.constructParametricType(parameterClass1, javaType);
    JavaType finalType = typeFactory.constructParametricType(parametrized, type);
    try {
      return IGNORE_NULL_MAPPER.readValue(jsonString, finalType);
    } catch (JsonProcessingException e) {
      throw new JsonParseException(e);
    }
  }

  @Nonnull
  public static <T> T parse(@Nonnull String jsonString, JavaType javaType) {
    try {
      return IGNORE_NULL_MAPPER.readValue(jsonString, javaType);
    } catch (JsonProcessingException e) {
      throw new JsonParseException(e);
    }
  }

  @Nonnull
  static <T> T parse(@Nonnull String jsonString, @Nonnull TypeReference<T> type) {
    try {
      return IGNORE_NULL_MAPPER.readValue(jsonString, type);
    } catch (JsonProcessingException e) {
      throw new JsonParseException(e);
    }
  }

  @Nonnull
  static <T> List<T> parseList(@Nonnull String jsonString, @Nonnull Class<T> clazz) {
    CollectionType type = IGNORE_NULL_MAPPER.getTypeFactory()
      .constructCollectionType(ArrayList.class, clazz);
    return parse(jsonString, type);
  }

  @Nonnull
  @SuppressWarnings("rawtypes")
  static <T> List<T> parseList(@Nonnull String jsonString,
                               @Nonnull Class<? extends List> listClass,
                               @Nonnull Class<T> elementClass) {
    CollectionType type = IGNORE_NULL_MAPPER.getTypeFactory()
      .constructCollectionType(listClass, elementClass);
    return parse(jsonString, type);
  }

  @Nonnull
  static <T> Set<T> parseSet(@Nonnull String jsonString, @Nonnull Class<T> clazz) {
    CollectionType type = IGNORE_NULL_MAPPER.getTypeFactory()
      .constructCollectionType(LinkedHashSet.class, clazz);
    return parse(jsonString, type);
  }

  @Nonnull
  @SuppressWarnings("rawtypes")
  static <T> Set<T> parseSet(@Nonnull String jsonString,
                             @Nonnull Class<? extends Set> listClass,
                             @Nonnull Class<T> elementClass) {
    CollectionType type = IGNORE_NULL_MAPPER.getTypeFactory()
      .constructCollectionType(listClass, elementClass);
    return parse(jsonString, type);
  }

  @Nonnull
  static <K, V> Map<K, V> parseMap(@Nonnull String jsonString,
                                   @Nonnull Class<K> keyClass,
                                   @Nonnull Class<V> valueClass) {
    return parseMap(jsonString, LinkedHashMap.class, keyClass, valueClass);
  }

  @Nonnull
  @SuppressWarnings("rawtypes")
  static <K, V> Map<K, V> parseMap(@Nonnull String jsonString,
                                   @Nonnull Class<? extends Map> mapClass,
                                   @Nonnull Class<K> keyClass,
                                   @Nonnull Class<V> valueClass) {
    MapType type = IGNORE_NULL_MAPPER.getTypeFactory()
      .constructMapType(mapClass, keyClass, valueClass);
    return parse(jsonString, type);
  }

  @Nonnull
  public static JsonNode readTree(@Nonnull String jsonString) {
    try {
      return IGNORE_NULL_MAPPER.readTree(jsonString);
    } catch (JsonProcessingException e) {
      throw new JsonParseException(e);
    }
  }

  @Nonnull
  public static JavaType constructJavaType(@Nonnull Type type) {
    if (type instanceof ParameterizedType) {
      ParameterizedType parameterizedType = (ParameterizedType) type;
      Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
      Class<?> rowClass = (Class<?>) parameterizedType.getRawType();
      JavaType[] javaTypes = new JavaType[actualTypeArguments.length];
      for (int i = 0; i < actualTypeArguments.length; i++) {
        javaTypes[i] = constructJavaType(actualTypeArguments[i]);
      }
      return TypeFactory.defaultInstance().constructParametricType(rowClass, javaTypes);
    } else {
      Class<?> cla = (Class<?>) type;
      return TypeFactory.defaultInstance().constructParametricType(cla, new JavaType[0]);
    }
  }
}
