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
package cn.idealframework.event.publisher;

import cn.idealframework.event.message.EventSupplier;
import cn.idealframework.event.message.EventSuppliers;
import cn.idealframework.lang.Lists;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

/**
 * 事件发布器接口
 *
 * @author 宋志宗 on 2021/4/22
 */
public interface EventPublisher {

  /**
   * 批量发布
   *
   * @param suppliers EventMessageSupplier Collection
   * @author 宋志宗 on 2021/10/19
   */
  void publish(@Nonnull Collection<EventSupplier> suppliers);

  default void publish(@Nonnull EventSuppliers suppliers) {
    List<EventSupplier> eventSuppliers = suppliers.get();
    publish(eventSuppliers);
  }

  default void publish(@Nonnull EventSupplier supplier) {
    publish(Lists.of(supplier));
  }
}
