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

import cn.idealframework.event.message.EventMessage;
import cn.idealframework.event.message.impl.GeneralEventMessage;
import cn.idealframework.event.publisher.AbstractEventPublisher;
import cn.idealframework.event.publisher.EventPublisher;
import cn.idealframework.event.publisher.transaction.AutomaticEventPublisher;
import cn.idealframework.event.publisher.transaction.EventTempDo;
import cn.idealframework.json.JsonUtils;
import lombok.extern.apachecommons.CommonsLog;

import javax.annotation.Nonnull;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * @author 宋志宗 on 2021/4/30
 */
@CommonsLog
public class DatabaseAutomaticEventPublisher implements AutomaticEventPublisher {
  /** 一次读取的最大消息数量 */
  private static final int BATCH_SIZE = 100;
  private volatile boolean running = false;

  private final AbstractEventPublisher eventPublisher;
  private final DataSource dataSource;
  /** 空闲时线程睡眠间隔 */
  private final long sleepMills;

  public DatabaseAutomaticEventPublisher(EventPublisher eventPublisher,
                                         DataSource dataSource) {
    this.eventPublisher = (AbstractEventPublisher) eventPublisher;
    this.dataSource = dataSource;
    sleepMills = 200;
  }

  public DatabaseAutomaticEventPublisher(EventPublisher eventPublisher,
                                         DataSource dataSource,
                                         long sleepMills) {
    this.eventPublisher = (AbstractEventPublisher) eventPublisher;
    this.dataSource = dataSource;
    if (sleepMills < 200) {
      sleepMills = 200;
    }
    this.sleepMills = sleepMills;
  }


  @Override
  public void start() {
    if (running) {
      return;
    }
    this.running = true;
    // lock to publish
    // To sleep if loaded event temps size is 0
    // Reset sleep flag
    Thread jobThread = new Thread(() -> {
      try {
        TimeUnit.MILLISECONDS.sleep(sleepMills);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      AtomicBoolean sleepFlag = new AtomicBoolean(false);
      while (running) {
        try (Connection connection = dataSource.getConnection()) {
          // lock to publish
          dbLock(connection, () -> {
            List<EventTempDo> eventTemps = loadEventTemps(connection);
            int size = eventTemps.size();
            if (size == 0) {
              sleepFlag.set(true);
            } else {
              long startTimeMillis = System.currentTimeMillis();
              List<EventMessage<?>> messages = eventTemps.stream()
                .map(temp -> {
                  String eventInfo = temp.getEventInfo();
                  return JsonUtils.parse(eventInfo, GeneralEventMessage.class);
                }).collect(Collectors.toList());
              eventPublisher.brokerPublish(messages);
              List<Long> tempIds = eventTemps.stream()
                .map(EventTempDo::getId)
                .collect(Collectors.toList());
              this.deletePublishedTemps(connection, tempIds);
              long consuming = System.currentTimeMillis() - startTimeMillis;
              log.debug("Read " + size + " messages from the database and publish them, It takes " + consuming + " milliseconds");
            }
          });
        } catch (Exception e) {
          e.printStackTrace();
          log.warn("dataSource.getConnection() ex:", e);
          sleepFlag.set(true);
        }
        // To sleep if loaded event temps size is 0
        if (sleepFlag.get()) {
          try {
            TimeUnit.MILLISECONDS.sleep(sleepMills);
          } catch (InterruptedException e) {
            log.warn("Thread sleep ex:", e);
          }
        }
        // Reset sleep flag
        sleepFlag.set(false);
      }
    });
//    jobThread.setDaemon(true);
    jobThread.setName("auto-publish");
    jobThread.start();
    Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
  }

  @Nonnull
  private List<EventTempDo> loadEventTemps(@Nonnull Connection connection) {
    String sql = "select id, event_info, timestamp from ideal_event_publish_temp order by id limit ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, BATCH_SIZE);
      try (ResultSet resultSet = statement.executeQuery()) {
        List<EventTempDo> res = new ArrayList<>();
        while (resultSet.next()) {
          long id = resultSet.getLong("id");
          String eventInfo = resultSet.getString("event_info");
          long timestamp = resultSet.getLong("timestamp");
          res.add(new EventTempDo(id, eventInfo, timestamp));
        }
        return res;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return Collections.emptyList();
    }
  }

  private void deletePublishedTemps(@Nonnull Connection connection, @Nonnull List<Long> tempIds) {
    String ids = tempIds.stream().map(id -> Long.toString(id)).collect(Collectors.joining(","));
    String sql = "delete from ideal_event_publish_temp where id in (" + ids + ")";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void dbLock(@Nonnull Connection connection, @Nonnull Runnable runnable) {
    String lockSql = "select lock_name from ideal_event_lock where lock_name = 'transaction_publish' for update";
    boolean rollback = false;
    boolean tempAutoCommit;
    try {
      tempAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);
    } catch (SQLException e) {
      return;
    }
    try (PreparedStatement preparedStatement = connection.prepareStatement(lockSql)) {
      preparedStatement.execute();
      try {
        runnable.run();
      } catch (Exception e) {
        rollback = true;
        log.warn("DB lock running exception: ", e);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        if (rollback) {
          log.debug("Rollback transaction");
          connection.rollback();
        } else {
          connection.commit();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
      try {
        connection.setAutoCommit(tempAutoCommit);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }


  public void stop() {
    this.running = false;
  }
}
