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
  `tenant_id`      bigint        NOT NULL COMMENT '租户id',
  `user_id`        bigint        NOT NULL COMMENT '用户id',
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
