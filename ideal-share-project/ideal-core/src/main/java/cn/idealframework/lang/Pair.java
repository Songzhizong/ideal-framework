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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * @author 宋志宗 on 2021/11/2
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pair<L, R> implements Map.Entry<L, R> {
  @Nonnull
  private L left;
  @Nonnull
  private R right;

  @Nonnull
  public static <L, R> Pair<L, R> of(@Nonnull L l, @Nonnull R r) {
    return new Pair<>(l, r);
  }

  @Nonnull
  @Override
  public L getKey() {
    return left;
  }

  @Nonnull
  @Override
  public R getValue() {
    return right;
  }

  @Nonnull
  @Override
  public R setValue(R value) {
    this.setRight(value);
    return right;
  }
}
