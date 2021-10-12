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
package cn.idealframework.trace;

import cn.idealframework.util.IpUtils;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 宋志宗 on 2021/6/1
 */
@Getter
@Setter
public class TraceInfo {
  private static final String SERVER_IP = IpUtils.getIp();

  /** 全链路唯一id */
  @Nonnull
  private String traceId;

  /** 范围id */
  @Nonnull
  private String spanId;

  /** 调用方式: http/bus/kafka... */
  @Nonnull
  private String mode;

  /** 应用名称 */
  @Nullable
  private String application;

  /** 调用目标 */
  @Nonnull
  private String target;

  /** http请求方ip地址 */
  @Nullable
  private String clientIp;

  /** 服务器ip */
  @Nonnull
  private String serverIp = SERVER_IP;

  /** 服务器端口号 */
  private int serverPort;

  @Nullable
  private List<String> messages;

  /** 请求参数 */
  @Nullable
  private Map<String, Object> request;

  /** 响应结果 */
  @Nullable
  private Object response;

  /** 总处理耗时 */
  private long consuming;

  /** 异常信息 */
  @Nullable
  private String exception;

  /** 执行时间 */
  @Nonnull
  private LocalDateTime executeTime;

  public synchronized void addMessage(@Nonnull String message) {
    if (this.messages == null) {
      messages = new ArrayList<>();
    }
    messages.add(message);
  }
}
