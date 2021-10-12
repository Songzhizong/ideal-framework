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
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author 宋志宗 on 2021/7/1
 */
@Getter
@Setter
public class IdealBootEventKafkaProperties {

  private List<String> bootstrapServers = new ArrayList<>(Collections.singletonList("localhost:9092"));

  private String clientId = "client-1";

  @NestedConfigurationProperty
  private IdealBootEventKafkaProducerProperties producer = new IdealBootEventKafkaProducerProperties();

  @NestedConfigurationProperty
  private IdealBootEventKafkaReceiveProperties receive = new IdealBootEventKafkaReceiveProperties();


}
