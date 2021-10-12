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
package cn.idealframework.boot.autoconfigure.web;

import cn.idealframework.json.JsonUtils;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2021/7/4
 */
@Getter
@Setter
public class MessageConverterProperties {
  /** 是否开启自定义消息转换器 */
  private boolean enableCustom = true;

  /** 序列化是忽略null */
  private boolean ignoreNull = false;

  /** 时间格式 */
  @Nonnull
  private String timePattern = JsonUtils.TIME_PATTERN;

  /** 日期格式 */
  @Nonnull
  private String datePattern = JsonUtils.DATE_PATTERN;

  /** 完整时间格式 */
  @Nonnull
  private String dateTimePattern = JsonUtils.DATE_TIME_PATTERN;
}
