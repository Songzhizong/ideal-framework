///*
// * Copyright 2021 cn.idealframework
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      https://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package cn.idealframework.operation;
//
//import cn.idealframework.database.DatabaseType;
//import cn.idealframework.lang.StringUtils;
//import lombok.extern.apachecommons.CommonsLog;
//
//import javax.annotation.Nonnull;
//import javax.sql.DataSource;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.sql.Timestamp;
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @author 宋志宗 on 2021/6/4
// */
//@CommonsLog
//public class OperationLogStorageDataSourceImpl implements OperationLogStorage {
//  private final Map<DatabaseType, String> sqlmap = new HashMap<>();
//  /** 数据源 */
//  private final DataSource dataSource;
//  /** 数据库类型 */
//  private final DatabaseType databaseType;
//
//  public OperationLogStorageDataSourceImpl(@Nonnull DataSource dataSource,
//                                           @Nonnull DatabaseType databaseType) {
//    this("ideal_user_operation_log", dataSource, databaseType);
//  }
//
//  public OperationLogStorageDataSourceImpl(@Nonnull String tableName,
//                                           @Nonnull DataSource dataSource,
//                                           @Nonnull DatabaseType databaseType) {
//    this.dataSource = dataSource;
//    this.databaseType = databaseType;
//    sqlmap.put(DatabaseType.MYSQL, "insert into " + tableName +
//      " (trace_id, `system`, operation, description, uri, tenant_id, " +
//      "user_id, username, client_ip, user_agent, success, message, operation_time) " +
//      "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
//
//    sqlmap.put(DatabaseType.POSTGRES, "");
//  }
//
//
//  @Override
//  public void save(@Nonnull OperationLog operationLog) throws Exception {
//    String sql = sqlmap.get(databaseType);
//    if (StringUtils.isBlank(sql)) {
//      throw new IllegalArgumentException(databaseType + " sql is blank");
//    }
//    try (Connection connection = dataSource.getConnection();
//         PreparedStatement statement = connection.prepareStatement(sql)) {
//      String traceId = operationLog.getTraceId();
//      if (StringUtils.isBlank(traceId)) {
//        traceId = "";
//      }
//      statement.setString(1, traceId);
//      String system = operationLog.getSystem();
//      if (StringUtils.isBlank(system)) {
//        system = "";
//      }
//      statement.setString(2, system);
//      String operation = operationLog.getOperation();
//      if (StringUtils.isBlank(operation)) {
//        operation = "";
//      }
//      statement.setString(3, operation);
//      String description = operationLog.getDescription();
//      if (StringUtils.isBlank(description)) {
//        description = "";
//      }
//      statement.setString(4, description);
//      String uri = operationLog.getUri();
//      if (StringUtils.isBlank(uri)) {
//        uri = "";
//      }
//      statement.setString(5, uri);
//      String tenantId = operationLog.getTenantId();
//      if (StringUtils.isBlank(tenantId)) {
//        tenantId = "";
//      }
//      statement.setString(6, tenantId);
//      String userId = operationLog.getUserId();
//      if (StringUtils.isBlank(userId)) {
//        userId = "";
//      }
//      statement.setString(7, userId);
//      String username = operationLog.getUsername();
//      if (StringUtils.isBlank(username)) {
//        username = "";
//      }
//      statement.setString(8, username);
//      String clientIp = operationLog.getClientIp();
//      if (StringUtils.isBlank(clientIp)) {
//        clientIp = "";
//      }
//      statement.setString(9, clientIp);
//      String userAgent = operationLog.getUserAgent();
//      if (StringUtils.isBlank(userAgent)) {
//        userAgent = "";
//      }
//      statement.setString(10, userAgent);
//      boolean success = operationLog.isSuccess();
//      statement.setInt(11, success ? 1 : 0);
//      String message = operationLog.getMessage();
//      if (StringUtils.isBlank(message)) {
//        message = "";
//      }
//      statement.setString(12, message);
//      LocalDateTime operationTime = operationLog.getOperationTime();
//      statement.setTimestamp(13, Timestamp.valueOf(operationTime));
//      boolean autoCommit = connection.getAutoCommit();
//      try {
//        statement.execute();
//        if (!autoCommit) {
//          connection.commit();
//        }
//      } catch (SQLException exception) {
//        connection.rollback();
//        throw exception;
//      }
//    }
//  }
//}
