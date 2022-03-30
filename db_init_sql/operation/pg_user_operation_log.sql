CREATE SEQUENCE "public"."ideal_user_operation_log_id_sequence" INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;

CREATE TABLE "public"."ideal_user_operation_log"
(
  "id" int8 NOT NULL DEFAULT nextval('ideal_user_operation_log_id_sequence'::regclass),
  "trace_id"       varchar(64) COLLATE "pg_catalog"."default"   NOT NULL,
  "system"         varchar(64) COLLATE "pg_catalog"."default"   NOT NULL,
  "operation"      varchar(100) COLLATE "pg_catalog"."default"  NOT NULL,
  "description"    varchar(4096) COLLATE "pg_catalog"."default" NOT NULL,
  "uri"            varchar(255) COLLATE "pg_catalog"."default"  NOT NULL,
  "tenant_id" int8 COLLATE "pg_catalog"."default" NOT NULL,
  "user_id" int8 COLLATE "pg_catalog"."default" NOT NULL,
  "username"       varchar(64) COLLATE "pg_catalog"."default"   NOT NULL,
  "client_ip"      varchar(255) COLLATE "pg_catalog"."default"  NOT NULL,
  "user_agent"     varchar(255) COLLATE "pg_catalog"."default"  NOT NULL,
  "success" int4 NOT NULL,
  "message"        varchar(4096) COLLATE "pg_catalog"."default" NOT NULL,
  "operation_time" timestamp(6)                                 NOT NULL
);
COMMENT
  ON COLUMN "public"."ideal_user_operation_log"."id" IS '主键';
COMMENT
  ON COLUMN "public"."ideal_user_operation_log"."trace_id" IS '操作事务id';
COMMENT
  ON COLUMN "public"."ideal_user_operation_log"."system" IS '所属系统';
COMMENT
  ON COLUMN "public"."ideal_user_operation_log"."operation" IS '操作名称';
COMMENT
  ON COLUMN "public"."ideal_user_operation_log"."description" IS '操作描述';
COMMENT
  ON COLUMN "public"."ideal_user_operation_log"."uri" IS '请求地址';
COMMENT
  ON COLUMN "public"."ideal_user_operation_log"."tenant_id" IS '租户id';
COMMENT
  ON COLUMN "public"."ideal_user_operation_log"."user_id" IS '用户id';
COMMENT
  ON COLUMN "public"."ideal_user_operation_log"."username" IS '用户姓名';
COMMENT
  ON COLUMN "public"."ideal_user_operation_log"."client_ip" IS '客户端ip';
COMMENT
  ON COLUMN "public"."ideal_user_operation_log"."user_agent" IS '客户端浏览器ua';
COMMENT
  ON COLUMN "public"."ideal_user_operation_log"."success" IS '是否执行成功';
COMMENT
  ON COLUMN "public"."ideal_user_operation_log"."message" IS '执行信息, 可用于记录错误信息';
COMMENT
  ON COLUMN "public"."ideal_user_operation_log"."operation_time" IS '操作时间';

CREATE
  INDEX "tenant_id" ON "public"."ideal_user_operation_log" USING btree
  (
  "tenant_id"
  COLLATE "pg_catalog"."default"
  "pg_catalog"."text_ops" ASC
  NULLS LAST
  );
CREATE
  INDEX "trace_id" ON "public"."ideal_user_operation_log" USING btree
  (
  "trace_id"
  COLLATE "pg_catalog"."default"
  "pg_catalog"."text_ops" ASC NULLS
  LAST
  );
CREATE
  INDEX "user_id" ON "public"."ideal_user_operation_log" USING btree
  (
  "user_id"
  COLLATE "pg_catalog"."default"
  "pg_catalog"."text_ops" ASC NULLS
  LAST
  );

ALTER TABLE "public"."ideal_user_operation_log"
  ADD CONSTRAINT "ideal_user_operation_log_pkey" PRIMARY KEY ("id");
