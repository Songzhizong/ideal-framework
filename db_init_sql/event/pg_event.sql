-- ----------------------------
-- Sequence structure for ideal_event_publish_temp_id_sequence
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."ideal_event_publish_temp_id_sequence";
CREATE SEQUENCE "public"."ideal_event_publish_temp_id_sequence" INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
ALTER SEQUENCE "public"."ideal_event_publish_temp_id_sequence" OWNER TO "postgres";

-- ----------------------------
-- Sequence structure for ideal_event_store_id_sequence
-- ----------------------------
DROP
  SEQUENCE IF EXISTS "public"."ideal_event_store_id_sequence";
CREATE
  SEQUENCE "public"."ideal_event_store_id_sequence"
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START
    1
  CACHE
    1;
ALTER
  SEQUENCE "public"."ideal_event_store_id_sequence" OWNER TO "postgres";

-- ----------------------------
-- Table structure for ideal_event_lock
-- ----------------------------
DROP TABLE IF EXISTS "public"."ideal_event_lock";
CREATE TABLE "public"."ideal_event_lock"
(
  "lock_name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL
)
;
ALTER TABLE "public"."ideal_event_lock"
  OWNER TO "postgres";

-- ----------------------------
-- Records of ideal_event_lock
-- ----------------------------
BEGIN;
INSERT INTO "public"."ideal_event_lock"
VALUES ('transaction_publish');
COMMIT;

-- ----------------------------
-- Table structure for ideal_event_publish_temp
-- ----------------------------
DROP TABLE IF EXISTS "public"."ideal_event_publish_temp";
CREATE TABLE "public"."ideal_event_publish_temp"
(
  "id"         int8                                NOT NULL DEFAULT nextval('ideal_event_publish_temp_id_sequence'::regclass),
  "event_info" text COLLATE "pg_catalog"."default" NOT NULL,
  "timestamp"  int8                                NOT NULL
)
;
ALTER TABLE "public"."ideal_event_publish_temp"
  OWNER TO "postgres";

-- ----------------------------
-- Records of ideal_event_publish_temp
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for ideal_event_store
-- ----------------------------
DROP TABLE IF EXISTS "public"."ideal_event_store";
CREATE TABLE "public"."ideal_event_store"
(
  "id"             int8                                        NOT NULL DEFAULT nextval('ideal_event_store_id_sequence'::regclass),
  "uuid"           varchar(64) COLLATE "pg_catalog"."default"  NOT NULL,
  "topic"          varchar(128) COLLATE "pg_catalog"."default" NOT NULL,
  "aggregate_type" varchar(64) COLLATE "pg_catalog"."default"  NOT NULL,
  "aggregate_id"   varchar(64) COLLATE "pg_catalog"."default"  NOT NULL,
  "headers"        text COLLATE "pg_catalog"."default"         NOT NULL,
  "payload"        text COLLATE "pg_catalog"."default"         NOT NULL,
  "event_time"     int8                                        NOT NULL
)
;
ALTER TABLE "public"."ideal_event_store"
  OWNER TO "postgres";

-- ----------------------------
-- Records of ideal_event_store
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
SELECT setval('"public"."ideal_event_publish_temp_id_sequence"', 2, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
SELECT setval('"public"."ideal_event_store_id_sequence"', 2, true);

-- ----------------------------
-- Primary Key structure for table ideal_event_lock
-- ----------------------------
ALTER TABLE "public"."ideal_event_lock"
  ADD CONSTRAINT "ideal_event_lock_pkey" PRIMARY KEY ("lock_name");

-- ----------------------------
-- Primary Key structure for table ideal_event_publish_temp
-- ----------------------------
ALTER TABLE "public"."ideal_event_publish_temp"
  ADD CONSTRAINT "ideal_event_publish_temp_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table ideal_event_store
-- ----------------------------
CREATE INDEX "event_time" ON "public"."ideal_event_store" USING btree
  (
   "event_time"
   "pg_catalog"."int8_ops" ASC
   NULLS LAST
    );
CREATE INDEX "topic" ON "public"."ideal_event_store" USING btree
  (
   "topic"
   COLLATE "pg_catalog"."default"
   "pg_catalog"."text_ops" ASC NULLS
   LAST
    );

-- ----------------------------
-- Primary Key structure for table ideal_event_store
-- ----------------------------
ALTER TABLE "public"."ideal_event_store"
  ADD CONSTRAINT "ideal_event_store_pkey" PRIMARY KEY ("id");
