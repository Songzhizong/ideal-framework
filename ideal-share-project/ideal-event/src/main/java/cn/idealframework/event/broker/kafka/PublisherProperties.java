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
package cn.idealframework.event.broker.kafka;

import lombok.*;

import java.time.Duration;
import java.util.Collections;
import java.util.Set;

/**
 * @author 宋志宗 on 2021/4/24
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublisherProperties {
  private String defaultEventTopic;
  private Set<String> bootstrapServers = Collections.singleton("localhost:9092");
  @SuppressWarnings("SpellCheckingInspection")
  private String acks = "1";
  private String clientId = "client-1";
  private long batchSize = 16 << 10;
  private Duration lingerMs = Duration.ZERO;
  private long bufferMemory = 32 << 20;
  private String compressionType = "none";
  private Integer retries = 2147483647;
}
