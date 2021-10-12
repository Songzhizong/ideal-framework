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
package cn.idealframework.authentication;

import lombok.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Set;

/**
 * @author 宋志宗 on 2021/6/23
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Certificate {
  /** ak */
  @Nonnull
  private String accessKey;
  /** sk */
  @Nonnull
  private String secretKey;
  /** -1表示永不过期 */
  private long expireTimestamp = -1;
  /** 允许的ip列表, 空代表全部 */
  @Nullable
  private Set<String> allowIps;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Certificate that = (Certificate) o;
    return accessKey.equals(that.accessKey);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accessKey);
  }
}
