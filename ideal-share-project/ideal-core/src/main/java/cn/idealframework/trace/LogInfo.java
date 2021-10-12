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
package cn.idealframework.trace;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;

/**
 * @author 宋志宗 on 2021/6/8
 */
@Getter
@Setter
public class LogInfo {
  @Nonnull
  private String traceId;

  @Nonnull
  private String spanId;

  /** logger名称 */
  @Nonnull
  private String loggerName;

  /** 线程名称 */
  @Nonnull
  private String threadName;

  /** 日志等级 */
  @Nonnull
  private String level;

  /** 日志内容 */
  @Nonnull
  private String message;

  /** 日志打印时间 */
  @Nonnull
  private LocalDateTime logTime;
}
