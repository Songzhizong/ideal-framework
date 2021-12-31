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
package cn.idealframework.lang;

import javax.annotation.Nullable;
import java.util.regex.Pattern;

/**
 * @author 宋志宗 on 2021/12/29
 */
public final class Numbers {
  private static final Pattern IS_NUMBER_PATTERN = Pattern.compile("^[-+]?[0]*[\\d]{1,19}$");

  private Numbers() {
  }

  public static boolean isNumber(@Nullable String str) {
    if (StringUtils.isBlank(str)) {
      return false;
    }
    boolean isNumber = IS_NUMBER_PATTERN.matcher(str).matches();
    if (isNumber) {
      try {
        Long.parseLong(str);
      } catch (NumberFormatException e) {
        isNumber = false;
      }
    }
    return isNumber;
  }
}
