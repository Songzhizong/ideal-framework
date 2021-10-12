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

import cn.idealframework.transmission.Result;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 宋志宗 on 2021/8/5
 */
public abstract class TypeReference<T> extends com.fasterxml.jackson.core.type.TypeReference<T> {

  public static final TypeReference<Map<String, String>> STRING_MAP_REFERENCE
      = new TypeReference<Map<String, String>>() {
  };

  public static final TypeReference<List<String>> STRING_LIST_REFERENCE
      = new TypeReference<List<String>>() {
  };

  public static final TypeReference<Set<String>> STRING_SET_REFERENCE
      = new TypeReference<Set<String>>() {
  };


  public static final TypeReference<Result<Void>> VOID_RESULT_REFERENCE = new TypeReference<Result<Void>>() {
  };

  public static final TypeReference<Result<String>> STRING_RESULT_REFERENCE = new TypeReference<Result<String>>() {
  };

  public static final TypeReference<Result<Long>> LONG_RESULT_REFERENCE = new TypeReference<Result<Long>>() {
  };

  public static final TypeReference<Result<Integer>> INTEGER_RESULT_REFERENCE = new TypeReference<Result<Integer>>() {
  };
}
