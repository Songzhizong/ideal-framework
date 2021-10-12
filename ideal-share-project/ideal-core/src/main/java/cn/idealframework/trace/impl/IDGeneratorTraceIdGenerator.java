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
package cn.idealframework.trace.impl;

import cn.idealframework.id.IDGenerator;
import cn.idealframework.trace.TraceIdGenerator;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2021/5/31
 */
@RequiredArgsConstructor
public class IDGeneratorTraceIdGenerator implements TraceIdGenerator {
  private final IDGenerator idGenerator;

  @Nonnull
  @Override
  public String generateTraceId() {
    return String.valueOf(idGenerator.generate());
  }
}
