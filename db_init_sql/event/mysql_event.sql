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
