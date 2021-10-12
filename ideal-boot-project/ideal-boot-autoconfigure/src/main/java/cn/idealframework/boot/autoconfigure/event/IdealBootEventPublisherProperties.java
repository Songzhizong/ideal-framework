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
package cn.idealframework.boot.autoconfigure.event;

import cn.idealframework.boot.autoconfigure.event.persistent.IdealBootEventPersistentProperties;
import cn.idealframework.boot.autoconfigure.event.transaction.IdealBootEventTransactionProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author 宋志宗 on 2021/7/1
 */
@Getter
@Setter
public class IdealBootEventPublisherProperties {

  /** 持久化配置 */
  @NestedConfigurationProperty
  private IdealBootEventPersistentProperties persistent = new IdealBootEventPersistentProperties();

  /** 事务配置 */
  @NestedConfigurationProperty
  private IdealBootEventTransactionProperties transaction = new IdealBootEventTransactionProperties();
}
