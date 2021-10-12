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

import cn.idealframework.cache.impl.SmartMemoryCacheBuilder;

import javax.annotation.Nonnull;

/**
 * 内存缓存接口
 *
 * @author 宋志宗 on 2021/7/10
 */
public interface MemoryCache<V> extends Cache<V> {

  @Nonnull
  static <V> MemoryCacheBuilder<V> newBuilder() {
    return SmartMemoryCacheBuilder.newBuilder();
  }

}
