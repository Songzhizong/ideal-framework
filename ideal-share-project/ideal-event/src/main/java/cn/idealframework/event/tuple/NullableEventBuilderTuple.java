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
package cn.idealframework.event.tuple;

import cn.idealframework.event.message.EventMessageBuilder;
import cn.idealframework.lang.Lists;
import lombok.Getter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

/**
 * @author 宋志宗 on 2021/4/27
 */
@Getter
public class NullableEventBuilderTuple<R> {
  /** 方法返回结果 */
  @Nullable
  private final R result;

  /** 方法返回事件列表 */
  @Nonnull
  private final List<EventMessageBuilder> eventBuilders;

  protected NullableEventBuilderTuple(@Nullable R result,
                                      @Nonnull List<EventMessageBuilder> eventBuilders) {
    this.result = result;
    this.eventBuilders = eventBuilders;
  }

  @Nonnull
  public static <R> NullableEventBuilderTuple<R> of(@Nullable R result,
                                                    @Nonnull List<EventMessageBuilder> builders) {
    return new NullableEventBuilderTuple<>(result, builders);
  }

  @Nonnull
  public static <R> NullableEventBuilderTuple<R> of(@Nullable R result,
                                                    @Nonnull EventMessageBuilder builder) {
    return new NullableEventBuilderTuple<>(result, Lists.of(builder));
  }

  @Nonnull
  public static <R> NullableEventBuilderTuple<R> of(@Nullable R result,
                                                    @Nonnull EventMessageBuilder... builders) {
    return new NullableEventBuilderTuple<>(result, Arrays.asList(builders));
  }
}
