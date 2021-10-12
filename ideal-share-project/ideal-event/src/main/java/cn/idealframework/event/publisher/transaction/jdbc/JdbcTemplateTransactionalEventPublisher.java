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
package cn.idealframework.event.publisher.transaction.jdbc;

import cn.idealframework.event.message.DomainEvent;
import cn.idealframework.event.message.EventMessage;
import cn.idealframework.event.persistence.EventMessageRepository;
import cn.idealframework.event.publisher.AbstractEventPublisher;
import cn.idealframework.event.publisher.TransactionalEventPublisher;
import cn.idealframework.json.JsonUtils;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author 宋志宗 on 2021/4/30
 */
@CommonsLog
public class JdbcTemplateTransactionalEventPublisher
    extends AbstractEventPublisher implements TransactionalEventPublisher {
  private final JdbcTemplate jdbcTemplate;
  private final int[] argTypes = new int[]{Types.VARCHAR, Types.BIGINT};

  public JdbcTemplateTransactionalEventPublisher(@Nonnull JdbcTemplate jdbcTemplate,
                                                 @Nullable EventMessageRepository eventMessageRepository) {
    super(eventMessageRepository);
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public void directPublish(@Nonnull Collection<EventMessage<? extends DomainEvent>> messages) {
    String sql = "insert into ideal_event_publish_temp (event_info, timestamp) values (?, ?)";
    List<Object[]> batchArgs = new ArrayList<>();
    for (EventMessage<? extends DomainEvent> message : messages) {
      String eventInfo = JsonUtils.toJsonStringIgnoreNull(message);
      long timestamp = System.currentTimeMillis();
      Object[] objects = {eventInfo, timestamp};
      batchArgs.add(objects);
    }
    jdbcTemplate.batchUpdate(sql, batchArgs, argTypes);
    log.debug("Batch save " + batchArgs.size() + " messages to temp table");
  }
}
