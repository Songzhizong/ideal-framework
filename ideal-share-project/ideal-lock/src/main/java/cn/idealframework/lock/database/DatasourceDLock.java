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
import lombok.extern.apachecommons.CommonsLog;

import javax.annotation.Nonnull;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author 宋志宗 on 2021/6/27
 */
@CommonsLog
public class DatasourceDLock implements DLock {
  private final Lock lock = new ReentrantLock();
  private final String name;
  private final String lockKey;
  private final DataSource dataSource;

  private volatile Boolean autoCommit = null;
  private volatile Thread currentThread = null;
  private volatile Connection currentConnection = null;
  private volatile PreparedStatement currentStatement = null;

  public DatasourceDLock(@Nonnull String name,
                         @Nonnull String lockKey,
                         @Nonnull DataSource dataSource) {
    this.name = name;
    this.lockKey = lockKey;
    this.dataSource = dataSource;
  }

  @Nonnull
  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean tryLock(@Nonnull Duration waitTime) throws InterruptedException {
    // 本地线程竞争, 竞争失败直接返回false
    long waitTimeMillis = waitTime.toMillis();
    if (waitTimeMillis < 1000) {
      waitTimeMillis = 1000;
    }
    boolean tryLock = lock.tryLock(waitTimeMillis, TimeUnit.MILLISECONDS);
    if (!tryLock) {
      return false;
    }
    if (Thread.currentThread() == currentThread) {
      return true;
    }
    String sql = "select * from ideal_dlock where lock_name = ? for update";
    try {
      currentConnection = dataSource.getConnection();
      autoCommit = currentConnection.getAutoCommit();
      currentConnection.setAutoCommit(false);
      currentStatement = currentConnection.prepareStatement(sql);
      currentStatement.setQueryTimeout((int) (waitTimeMillis / 1000));
      currentStatement.setString(1, lockKey);
      try {
        currentStatement.execute();
        return true;
      } catch (SQLException exception) {
        unlockAll();
        return false;
      }
    } catch (SQLException exception) {
      log.info("Lock exception:", exception);
      unlockAll();
      return false;
    }
  }

  @Override
  public void forceUnlock() {
    unlockAll();
  }

  @Override
  public void lock() {
    lock.lock();
    if (Thread.currentThread() == currentThread) {
      return;
    }
    String sql = "select * from ideal_dlock where lock_name = ? for update";
    try {
      currentConnection = dataSource.getConnection();
      autoCommit = currentConnection.getAutoCommit();
      currentConnection.setAutoCommit(false);
      currentStatement = currentConnection.prepareStatement(sql);
      currentStatement.setString(1, lockKey);
      currentStatement.execute();
    } catch (SQLException exception) {
      log.info("Lock exception:", exception);
      unlockAll();
      throw new RuntimeException(exception);
    }
  }

  @Override
  public boolean tryLock() {
    // 本地线程竞争, 竞争失败直接返回false
    boolean tryLock = lock.tryLock();
    if (!tryLock) {
      return false;
    }
    if (Thread.currentThread() == currentThread) {
      return true;
    }
    String sql = "select lock_name from ideal_dlock where lock_name = ? for update nowait";
    try {
      currentConnection = dataSource.getConnection();
      autoCommit = currentConnection.getAutoCommit();
      currentConnection.setAutoCommit(false);
      currentStatement = currentConnection.prepareStatement(sql);
      currentStatement.setString(1, lockKey);
      try {
        currentStatement.execute();
        return true;
      } catch (SQLException exception) {
        unlockAll();
        return false;
      }
    } catch (SQLException exception) {
      log.info("Lock exception:", exception);
      unlockAll();
      return false;
    }
  }

  @Override
  public void unlock() {
    if (Thread.currentThread() == currentThread) {
      dbUnlock();
    }
    lock.unlock();
  }

  private void unlockAll() {
    dbUnlock();
    lock.unlock();
  }

  private void dbUnlock() {
    try {
      currentConnection.commit();
    } catch (SQLException exception) {
      log.warn("Commit exception:", exception);
    }

    if (autoCommit != null) {
      try {
        currentConnection.setAutoCommit(autoCommit);
      } catch (SQLException exception) {
        log.warn("Set autoCommit exception:", exception);
      }
    }

    if (currentStatement != null) {
      try {
        currentStatement.close();
      } catch (SQLException exception) {
        log.warn("Close statement exception:", exception);
      }
    }

    if (currentConnection != null) {
      try {
        currentConnection.close();
      } catch (SQLException exception) {
        log.warn("Close connection exception:", exception);
      }
    }

    reset();
  }

  private void reset() {
    autoCommit = null;
    currentThread = null;
    currentConnection = null;
    currentStatement = null;
  }
}
