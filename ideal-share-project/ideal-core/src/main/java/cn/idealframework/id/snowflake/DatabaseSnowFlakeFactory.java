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
package cn.idealframework.id.snowflake;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;

import javax.annotation.Nonnull;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * 基于数据库实现的id生成器工厂
 *
 * @author 宋志宗 on 2021/6/1
 */
@CommonsLog
public class DatabaseSnowFlakeFactory implements SnowflakeFactory, SnowflakeMachineIdHolder {
  /** 默认30分钟未心跳则删除注册机器码 */
  private static final long EXPIRE_MILLIS = 1800_000;
  /** 默认心跳间隔30秒 */
  private static final long RENEWAL_INTERVAL_MILLIS = 30_000;
  private static final DatabaseSql DATABASE_SQL = DatabaseSql.create(
    "INSERT INTO ideal_snowflake_machine_id_registry (application, machine_id, last_heartbeat, signature) values (?, ?, ?, ?)",
    "UPDATE ideal_snowflake_machine_id_registry SET last_heartbeat = ? WHERE application = ? AND machine_id = ? AND signature = ?",
    "DELETE FROM ideal_snowflake_machine_id_registry WHERE application = ? AND machine_id = ? AND signature = ?",
    "DELETE FROM ideal_snowflake_machine_id_registry WHERE last_heartbeat < ?"
  );

  private final Map<String, SnowFlake> idGeneratorMap = new ConcurrentHashMap<>();
  private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
  private final String signature = UUID.randomUUID().toString();
  private final DataSource dataSource;
  private final long dataCenterId;
  private long machineId;
  private final String applicationName;
  private volatile boolean released = false;

  /**
   * @param dataCenterId    数据中心id
   * @param applicationName 服务名称
   * @param dataSource      数据源
   */
  public DatabaseSnowFlakeFactory(long dataCenterId,
                                  @Nonnull String applicationName,
                                  @Nonnull DataSource dataSource) {
    this.dataCenterId = dataCenterId;
    this.applicationName = applicationName;
    this.dataSource = dataSource;
    this.clearExpired();
    this.machineId = calculateMachineId();
    scheduledExecutorService.scheduleAtFixedRate(() -> {
      try {
        this.heartbeat();
        this.clearExpired();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }, RENEWAL_INTERVAL_MILLIS, RENEWAL_INTERVAL_MILLIS, TimeUnit.MILLISECONDS);
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      this.release();
      scheduledExecutorService.shutdown();
    }));
  }

  @Nonnull
  @Override
  public SnowFlake getGenerator(@Nonnull String biz) {
    return idGeneratorMap.computeIfAbsent(biz, k -> new SnowFlake(dataCenterId, this));
  }

  @Override
  public synchronized void release() {
    if (released) {
      return;
    }
    String releaseSql = DATABASE_SQL.getReleaseSql();
    try (Connection connection = dataSource.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(releaseSql)) {
      preparedStatement.setString(1, applicationName);
      preparedStatement.setLong(2, machineId);
      preparedStatement.setString(3, signature);
      preparedStatement.execute();
      log.info("Release snowflake machineId. applicationName = " + applicationName + " machineId = " + machineId);
    } catch (SQLException exception) {
      log.warn("Release snowflake machineId ex: " + exception.getMessage());
    }
    released = true;
  }

  @Override
  public long getCurrentMachineId() {
    return machineId;
  }

  private void heartbeat() {
    long currentTimeMillis = System.currentTimeMillis();
    String heartbeatSql = DATABASE_SQL.getHeartbeatSql();
    try (Connection connection = dataSource.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(heartbeatSql)) {
      preparedStatement.setLong(1, currentTimeMillis);
      preparedStatement.setString(2, applicationName);
      preparedStatement.setLong(3, machineId);
      preparedStatement.setString(4, signature);
      int i = preparedStatement.executeUpdate();
      log.debug("heartbeat... " + i + " rows changed");
      if (i == 0) {
        this.machineId = calculateMachineId();
      }
    } catch (SQLException exception) {
      log.warn("heartbeat() ex: " + exception.getMessage());
    }
  }

  private long calculateMachineId() {
    Connection connection;
    try {
      connection = dataSource.getConnection();
    } catch (SQLException exception) {
      log.warn("dataSource.getConnection() ex: " + exception.getMessage());
      throw new RuntimeException(exception);
    }
    int maxMachineNum = SnowFlake.MAX_MACHINE_NUM;
    try {
      long machineId = -1;
      String sql = DATABASE_SQL.getInsertSql();
      while (true) {
        ++machineId;
        if (machineId > maxMachineNum) {
          log.error("SnowFlake machineId 计算失败,已达上限: " + maxMachineNum);
          return ThreadLocalRandom.current().nextLong(maxMachineNum);
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
          preparedStatement.setString(1, applicationName);
          preparedStatement.setLong(2, machineId);
          preparedStatement.setLong(3, System.currentTimeMillis());
          preparedStatement.setString(4, signature);
          preparedStatement.execute();
          log.info("Snowflake 机器码: " + machineId);
          return machineId;
        } catch (SQLException exception) {
          log.debug("尝试获取机器id: " + machineId + " 失败: " + exception.getMessage());
        }
      }
    } finally {
      try {
        connection.close();
      } catch (SQLException exception) {
        exception.printStackTrace();
      }
    }
  }

  private void clearExpired() {
    long maxHeartbeat = System.currentTimeMillis() - (EXPIRE_MILLIS);
    String clearSql = DATABASE_SQL.getClearSql();
    try (Connection connection = dataSource.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(clearSql)) {
      preparedStatement.setLong(1, maxHeartbeat);
      preparedStatement.execute();
    } catch (SQLException exception) {
      log.warn("clearExpired() ex: " + exception.getMessage());
    }
  }

  @Getter
  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  private static class DatabaseSql {
    /** 尝试新增数据 */
    private final String insertSql;
    /** 心跳sql */
    private final String heartbeatSql;
    /** 释放当前占用机器码SQL */
    private final String releaseSql;
    /** 清除过期机器码SQL */
    private final String clearSql;

    @Nonnull
    public static DatabaseSql create(@Nonnull String insertSql,
                                     @Nonnull String heartbeatSql,
                                     @Nonnull String releaseSql,
                                     @Nonnull String clearSql) {
      return new DatabaseSql(insertSql, heartbeatSql, releaseSql, clearSql);
    }
  }
}
