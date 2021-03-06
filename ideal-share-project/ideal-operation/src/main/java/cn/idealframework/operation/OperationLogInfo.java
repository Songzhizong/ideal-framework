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
package cn.idealframework.operation;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDateTime;

/**
 * @author 宋志宗 on 2021/6/4
 */
@Getter
@Setter
public class OperationLogInfo {

  @Nullable
  private String platform;

  /** 所属系统名称 */
  @Nullable
  private String systemName;

  /** 操作名称 */
  @Nonnull
  private String operation;

  /** 操作描述 */
  private String details;

  /** 请求地址 */
  @Nullable
  private String uri;

  /** 租户id */
  @Nullable
  private Long tenantId;

  /** 用户id */
  private long userId;

  /** 用户姓名 */
  @Nullable
  private String username;

  /** 原始请求ip */
  private String originalIp;

  /** 客户端浏览器UA */
  private String userAgent;

  /** 是否执行成功 */
  private boolean success;

  /** 执行信息, 可用于记录错误信息 */
  @Nullable
  private String message;

  /** 操作时间 */
  private LocalDateTime operationTime;
}
