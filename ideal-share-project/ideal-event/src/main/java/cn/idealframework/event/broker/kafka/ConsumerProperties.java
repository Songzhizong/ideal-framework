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
public class ConsumerProperties {
  private String groupId;
  private String clientId = "client-1";
  private Set<String> topics = Collections.emptySet();
  private Set<String> bootstrapServers = Collections.singleton("localhost:9092");
  private int corePoolSize = 0;
  private int maximumPoolSize = 64;
}
