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
package cn.idealframework.boot.autoconfigure.event.broker.properties.kafka;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.unit.DataSize;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 宋志宗 on 2021/7/1
 */
@Getter
@Setter
public class IdealBootEventKafkaProducerProperties {
  private String defaultEventTopic;

  @Nonnull
  private Map<String, String> topicMapping = new HashMap<>();

  @SuppressWarnings("SpellCheckingInspection")
  private String acks = "1";

  private DataSize batchSize = DataSize.ofKilobytes(16);

  private Duration lingerMs = Duration.ZERO;

  private DataSize bufferMemory = DataSize.ofMegabytes(32);

  private String compressionType = "none";

  private Integer retries = 2147483647;
}
