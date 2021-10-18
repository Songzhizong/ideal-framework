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
