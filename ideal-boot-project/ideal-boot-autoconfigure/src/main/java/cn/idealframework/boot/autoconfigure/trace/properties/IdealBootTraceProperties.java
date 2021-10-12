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
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

/**
 * @author 宋志宗 on 2021/6/5
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "ideal.trace")
public class IdealBootTraceProperties {

  private Set<String> excludePatterns = new HashSet<>();

  @Nonnull
  @NestedConfigurationProperty
  private ArgsRecordProperties argsRecord = new ArgsRecordProperties();

  /** 日志输出traceLog的配置 */
  @Nonnull
  @NestedConfigurationProperty
  private CollectorProperties collector = new CollectorProperties();
}
