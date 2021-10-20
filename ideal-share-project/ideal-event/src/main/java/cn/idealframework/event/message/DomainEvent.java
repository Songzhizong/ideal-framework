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
package cn.idealframework.event.message;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.beans.Transient;

/**
 * 领域事件接口
 *
 * @author 宋志宗 on 2021/4/22
 */
public interface DomainEvent extends EventSupplier {
  /**
   * @return 事件类型
   * @author 宋志宗 on 2021/4/22
   */
  @Nonnull
  @Transient
  String getTopic();

  /**
   * @return 聚合类型
   * @author 宋志宗 on 2021/4/22
   */
  @Nullable
  @Transient
  String getAggregateType();

  /**
   * @return 聚合的唯一id
   * @author 宋志宗 on 2021/4/22
   */
  @Nullable
  @Transient
  String getAggregateId();

  @Nonnull
  @Override
  default EventMessage<?> get() {
    return EventMessage.of(this);
  }
}
