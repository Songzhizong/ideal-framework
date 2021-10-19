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
package cn.idealframework.event.persistence.jdbc;

import cn.idealframework.event.message.EventHeaders;
import cn.idealframework.event.message.EventMessage;
import cn.idealframework.event.persistence.EventMessageRepository;
import cn.idealframework.json.JsonUtils;
import cn.idealframework.lang.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author 宋志宗 on 2021/4/27
 */
@CommonsLog
@RequiredArgsConstructor
public class JdbcTemplateEventMessageRepository implements EventMessageRepository {
  private final JdbcTemplate jdbcTemplate;

  @Override
  public void saveAll(@Nonnull Collection<EventMessage<?>> messages) {
    String sql = "insert into  ideal_event_store" +
      " (uuid, topic, aggregate_type, aggregate_id, headers, payload, event_time)" +
      " values (?, ?, ?, ?, ?, ?, ?)";
    List<Object[]> args = new ArrayList<>();
    for (EventMessage<?> message : messages) {
      Object[] objects = new Object[7];
      objects[0] = message.uuid();
      objects[1] = message.getTopic();
      String aggregateType = message.getAggregateType();
      objects[2] = StringUtils.isBlank(aggregateType) ? "" : aggregateType;
      String aggregateId = message.getAggregateId();
      objects[3] = StringUtils.isBlank(aggregateId) ? "" : aggregateId;
      EventHeaders headers = message.getHeaders();
      objects[4] = headers == null ? "{}" : JsonUtils.toJsonString(headers);
      objects[5] = JsonUtils.toJsonStringIgnoreNull(message.getPayload());
      objects[6] = message.getEventTime();
      args.add(objects);
    }
    jdbcTemplate.batchUpdate(sql, args);
  }

  @Override
  public int deleteByTopicAndEventTimeBefore(@Nonnull String topic, long eventTimeBefore) {
    String sql = "delete from ideal_event_store where topic = ? and event_time < ?";
    return jdbcTemplate.update(sql, topic, eventTimeBefore);
  }
}
