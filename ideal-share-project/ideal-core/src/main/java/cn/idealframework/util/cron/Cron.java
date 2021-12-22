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
package cn.idealframework.util.cron;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.apachecommons.CommonsLog;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2018-11-20 19:46
 */
@Getter
@Setter
@CommonsLog
@SuppressWarnings("unused")
public class Cron {
  @Nonnull
  private final String value;

  public Cron(@Nonnull String value) {
    this.value = value;
  }

  @Nonnull
  public static CronBuilder builder() {
    return new CronBuilder();
  }

  @Override
  public String toString() {
    return value;
  }
}
