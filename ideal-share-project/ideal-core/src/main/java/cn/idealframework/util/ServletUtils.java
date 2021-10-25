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
package cn.idealframework.util;

import cn.idealframework.lang.StringUtils;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;

/**
 * @author 宋志宗 on 2021/3/8
 * @deprecated see {@link cn.idealframework.extensions.ServletUtils}
 */
@Deprecated
public class ServletUtils {
  private static final String UNKNOWN = "UNKNOWN";

  /**
   * 获取原始的请求ip地址
   *
   * @param request 请求信息
   * @return 原始ip
   * @author 宋志宗 on 2021/3/8
   */
  public static String getOriginalIp(@Nonnull HttpServletRequest request) {
    String ip = request.getHeader("X-Forwarded-For");
    if (StringUtils.isNotBlank(ip)) {
      int index = ip.indexOf(',');
      if (index > -1) {
        String substring = ip.substring(0, index);
        if (!StringUtils.equalsIgnoreCase(substring, UNKNOWN)) {
          return substring;
        }
      } else {
        return ip;
      }
    }
    return request.getRemoteAddr();
  }


  private ServletUtils() {
  }
}
