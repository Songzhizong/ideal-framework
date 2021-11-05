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
package cn.idealframework.extensions.reactor;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Duration;

/**
 * @author 宋志宗 on 2021/9/17
 */
@Getter
@Setter
@Accessors(chain = true)
public class HttpClientOptions {

  @Nonnull
  private String name = "httpClient";

  /** 最大连接数 */
  private int maxConnections = 512;

  /** 等待队列大小, 默认情况下为最大连接数x2 */
  @Nullable
  private Integer pendingAcquireMaxCount;

  /** 请求超时时间 */
  @Nullable
  private Duration responseTimeout;

  /** http keep alive */
  private boolean keepAlive = true;

  /** 跟踪重定向 */
  private boolean followRedirect = false;

  /** 是否启用gzip支持 */
  private boolean compressionEnabled = false;
}
