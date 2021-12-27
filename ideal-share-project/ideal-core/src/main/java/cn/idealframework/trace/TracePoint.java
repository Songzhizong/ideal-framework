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

import java.lang.annotation.*;

/**
 * @author 宋志宗 on 2021/1/11
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TracePoint {

  /**
   * 是否记录入参
   */
  boolean recordReq() default true;

  /**
   * 是否记录入参
   */
  boolean recordResp() default false;

  /**
   * 需要排除的请求参数位置, 从0开始
   */
  int[] exclusionArgs() default {};

  /**
   * 排除所有请求参数
   */
  boolean excludeAllArgs() default false;
}
