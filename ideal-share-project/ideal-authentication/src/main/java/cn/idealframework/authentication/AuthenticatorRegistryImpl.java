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

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 宋志宗 on 2021/6/28
 */
public class AuthenticatorRegistryImpl implements AuthenticatorRegistry {
  private final Map<String, Authenticator> authenticatorMap = new ConcurrentHashMap<>();

  @Override
  public void register(@Nonnull Authenticator authenticator) {
    String signerType = authenticator.getSignerType();
    Authenticator exists = authenticatorMap.put(signerType, authenticator);
    if (exists != null) {
      throw new IllegalArgumentException("Repeated signerType: " + signerType + " class: " + authenticator.getClass().getName());
    }
  }

  @Nonnull
  @Override
  public Optional<Authenticator> get(@Nonnull String signerType) {
    return Optional.ofNullable(authenticatorMap.get(signerType));
  }
}
