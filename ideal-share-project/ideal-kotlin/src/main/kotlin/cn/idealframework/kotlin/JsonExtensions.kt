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
package cn.idealframework.kotlin

import cn.idealframework.json.JsonUtils
import cn.idealframework.json.TypeReference

fun Any.toJsonString() = JsonUtils.toJsonString(this)

fun <T> String.parseJson(type: TypeReference<T>) = JsonUtils.parse(this, type)

fun <T> String.parseJson(clazz: Class<T>) = JsonUtils.parse(this, clazz)

fun <T> String.parseJson(parametrized: Class<out Any>, parameterClass: Class<out Any>): T {
  return JsonUtils.parse<T>(this, parametrized, parameterClass)
}

fun <E> String.parseJsonList(clazz: Class<E>): List<E> = JsonUtils.parseList(this, clazz)

fun <E> String.parseJsonSet(clazz: Class<E>): Set<E> = JsonUtils.parseSet(this, clazz)
