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
package cn.idealframework.cache;

import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2022/1/25
 */
public class CacheUtils {
  public static final String NULL_VALUE = "$$ideal$$cache$$null$$value$$";

  public static boolean isNullValue(@Nullable String value) {
    return value == null || NULL_VALUE.equals(value);
  }

  public static boolean isNotNulValue(@Nullable String value) {
    return !isNullValue(value);
  }
}
