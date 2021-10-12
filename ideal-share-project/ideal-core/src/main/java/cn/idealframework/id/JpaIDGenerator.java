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
package cn.idealframework.id;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentityGenerator;

import javax.annotation.Nonnull;
import java.io.Serializable;

/**
 * @author 宋志宗 on 2020/11/12
 */
public class JpaIDGenerator extends IdentityGenerator {
  private static IDGenerator idGenerator;

  public static IDGenerator getIdGenerator() {
    return idGenerator;
  }

  public static void setIdGenerator(@Nonnull IDGenerator idGenerator) {
    JpaIDGenerator.idGenerator = idGenerator;
  }

  @Override
  public Serializable generate(SharedSessionContractImplementor s, Object obj) {
    return idGenerator.generate();
  }
}
