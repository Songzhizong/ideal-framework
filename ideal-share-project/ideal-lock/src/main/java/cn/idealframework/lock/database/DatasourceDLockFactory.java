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
package cn.idealframework.lock.database;

import cn.idealframework.lock.DLock;
import cn.idealframework.lock.DLockFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;

import javax.annotation.Nonnull;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 宋志宗 on 2021/6/27
 */
@CommonsLog
@RequiredArgsConstructor
public class DatasourceDLockFactory implements DLockFactory {
  private final Map<String, DLock> lockMap = new ConcurrentHashMap<>();

  private final String serviceName;
  private final DataSource dataSource;

  @Nonnull
  @Override
  public DLock getLock(@Nonnull String name) {
    String lockKey = serviceName + ":" + name;
    return lockMap.computeIfAbsent(name, k -> {
      initLockKey(lockKey);
      return new DatasourceDLock(name, lockKey, dataSource);
    });
  }

  @Nonnull
  @Override
  public DLock getLock(@Nonnull String name, @Nonnull Duration leaseTime) {
    return getLock(name);
  }

  private void initLockKey(@Nonnull String lockKey) {
    String sql = "insert into ideal_dlock (lock_name) values (?)";
    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, lockKey);
      try {
        statement.execute();
      } catch (SQLException exception) {
        log.info("insert ideal_dlock ex:", exception);
      }
      if (!connection.getAutoCommit()) {
        connection.commit();
      }
    } catch (SQLException exception) {
      log.warn("initLockKey ex:", exception);
    }
  }
}
