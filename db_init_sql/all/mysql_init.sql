-- ----------------------------
-- Table structure for ideal_event_lock
-- ----------------------------
DROP TABLE IF EXISTS `ideal_event_lock`;
CREATE TABLE `ideal_event_lock`
(
    `lock_name` varchar(64) NOT NULL,
    PRIMARY KEY (`lock_name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of ideal_event_lock
-- ----------------------------
BEGIN;
INSERT INTO `ideal_event_lock`
VALUES ('transaction_publish');
COMMIT;

-- ----------------------------
-- Table structure for ideal_event_publish_temp
-- ----------------------------
DROP TABLE IF EXISTS `ideal_event_publish_temp`;
CREATE TABLE `ideal_event_publish_temp`
(
    `id`         bigint   NOT NULL AUTO_INCREMENT,
    `event_info` longtext NOT NULL,
    `timestamp`  bigint   NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for ideal_event_store
-- ----------------------------
DROP TABLE IF EXISTS `ideal_event_store`;
CREATE TABLE `ideal_event_store`
(
    `id`             bigint       NOT NULL AUTO_INCREMENT,
    `uuid`           varchar(64)  NOT NULL,
    `topic`          varchar(128) NOT NULL,
    `aggregate_type` varchar(64)  NOT NULL,
    `aggregate_id`   varchar(64)  NOT NULL,
    `headers`        longtext     NOT NULL,
    `payload`        longtext     NOT NULL,
    `event_time`     bigint       NOT NULL,
    PRIMARY KEY (`id`),
    KEY `topic` (`topic`) USING BTREE,
    KEY `event_time` (`event_time`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for ideal_user_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `ideal_user_operation_log`;
CREATE TABLE `ideal_user_operation_log`
(
    `id`             bigint        NOT NULL AUTO_INCREMENT COMMENT '主键',
    `trace_id`       varchar(64)   NOT NULL COMMENT '操作事务id',
    `system`         varchar(64)   NOT NULL COMMENT '所属系统',
    `operation`      varchar(100)  NOT NULL COMMENT '操作名称',
    `description`    varchar(4096) NOT NULL COMMENT '操作描述',
    `uri`            varchar(255)  NOT NULL COMMENT '请求地址',
    `tenant_id`      varchar(64)   NOT NULL COMMENT '租户id',
    `user_id`        varchar(64)   NOT NULL COMMENT '用户id',
    `username`       varchar(64)   NOT NULL COMMENT '用户姓名',
    `client_ip`      varchar(255)  NOT NULL COMMENT '客户端ip',
    `user_agent`     varchar(255)  NOT NULL COMMENT '客户端浏览器ua',
    `success`        int           NOT NULL COMMENT '是否执行成功',
    `message`        varchar(4096) NOT NULL COMMENT '执行信息, 可用于记录错误信息',
    `operation_time` datetime      NOT NULL COMMENT '操作时间',
    PRIMARY KEY (`id`),
    KEY `trace_id` (`trace_id`) USING BTREE,
    KEY `tenant_id` (`tenant_id`) USING BTREE,
    KEY `user_id` (`user_id`) USING BTREE
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for ideal_snowflake_machine_id_registry
-- ----------------------------
DROP TABLE IF EXISTS `ideal_snowflake_machine_id_registry`;
CREATE TABLE `ideal_snowflake_machine_id_registry`
(
    `application`    varchar(255) NOT NULL,
    `machine_id`     int          NOT NULL,
    `last_heartbeat` bigint       NOT NULL,
    `signature`      varchar(255) NOT NULL,
    PRIMARY KEY (`application`, `machine_id`),
    KEY `ck_sa_app_mi` (`signature`, `application`, `machine_id`) USING BTREE
) ENGINE = InnoDB COMMENT = 'snowflake机器码注册信息';
