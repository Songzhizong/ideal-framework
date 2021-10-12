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
package cn.idealframework.boot.autoconfigure.trace.properties;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * @author 宋志宗 on 2021/6/4
 */
@Getter
@Setter
public class ArgsRecordProperties {
  /** 是否记录请求/响应参数 */
  private boolean enabled = true;

  /** 接口调用日志扫描包,默认全部接口 */
  @Nullable
  private Set<String> basePackages;

  /** 需要排除的包路径 */
  @Nullable
  private Set<String> excludePackages;
}
