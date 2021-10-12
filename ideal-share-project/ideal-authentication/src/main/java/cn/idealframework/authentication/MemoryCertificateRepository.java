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

import lombok.Setter;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * @author 宋志宗 on 2021/6/24
 */
public class MemoryCertificateRepository implements CertificateRepository {
  @Setter
  private Map<String, Certificate> certificateMap;

  public MemoryCertificateRepository(Map<String, Certificate> certificateMap) {
    if (certificateMap == null) {
      certificateMap = Collections.emptyMap();
    }
    this.certificateMap = certificateMap;
  }

  @Override
  public Optional<Certificate> getCertificate(@Nonnull String accessKey) {
    return Optional.ofNullable(certificateMap.get(accessKey));
  }
}
