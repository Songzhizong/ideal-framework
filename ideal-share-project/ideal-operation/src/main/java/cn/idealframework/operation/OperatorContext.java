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

import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2021/6/4
 */
public interface OperatorContext {

  /**
   * 所属平台
   */
  @Nullable
  String platform();

  /**
   * 获取系统名称, 优先从注解中获取
   *
   * @return 系统名称
   */
  @Nullable
  String getSystem();

  /**
   * @return 用户唯一ID
   */
  long getUserId();

  /**
   * @return 用户姓名
   */
  @Nullable
  String getUsername();

  /**
   * @return 租户id
   */
  @Nullable
  Long getTenantId();
}
