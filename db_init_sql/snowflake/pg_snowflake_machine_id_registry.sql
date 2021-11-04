-- ----------------------------
-- Table structure for ideal_snowflake_machine_id_registry
-- ----------------------------
DROP TABLE IF EXISTS "public"."ideal_snowflake_machine_id_registry";
CREATE TABLE "public"."ideal_snowflake_machine_id_registry"
(
  "application"    varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "machine_id"     int4                                        NOT NULL,
  "last_heartbeat" int8                                        NOT NULL,
  "signature"      varchar(255) COLLATE "pg_catalog"."default" NOT NULL
);
COMMENT ON COLUMN "public"."ideal_snowflake_machine_id_registry"."application" IS '应用名称';
COMMENT ON COLUMN "public"."ideal_snowflake_machine_id_registry"."machine_id" IS '机器id';
COMMENT ON COLUMN "public"."ideal_snowflake_machine_id_registry"."last_heartbeat" IS '最近心跳时间';
COMMENT ON COLUMN "public"."ideal_snowflake_machine_id_registry"."signature" IS '签名';

-- ----------------------------
-- Indexes structure for table ideal_snowflake_machine_id_registry
-- ----------------------------
CREATE INDEX "ck_sa_app_mi" ON "public"."ideal_snowflake_machine_id_registry" USING btree
  (
   "signature"
   COLLATE "pg_catalog"."default"
   "pg_catalog"."text_ops"
   ASC NULLS LAST,
   "application"
   COLLATE "pg_catalog"."default"
   "pg_catalog"."text_ops"
   ASC NULLS LAST,
   "machine_id"
   "pg_catalog"."int4_ops"
   ASC NULLS LAST
    );

-- ----------------------------
-- Primary Key structure for table ideal_snowflake_machine_id_registry
-- ----------------------------
ALTER TABLE "public"."ideal_snowflake_machine_id_registry"
  ADD CONSTRAINT "ideal_snowflake_machine_id_registry_pkey" PRIMARY KEY ("application", "machine_id");
