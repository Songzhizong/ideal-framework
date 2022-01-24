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
package cn.idealframework.database.jpa.converter;

import cn.idealframework.lang.Joiner;
import cn.idealframework.lang.Sets;
import cn.idealframework.lang.StringUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author 宋志宗 on 2021/11/2
 */
@Converter
public class LongSetConverter implements AttributeConverter<Set<Long>, String> {

  @Override
  public String convertToDatabaseColumn(Set<Long> attribute) {
    if (Sets.isEmpty(attribute)) {
      return "";
    }
    return Joiner.joinSkipNull(attribute, ",");
  }

  @Override
  public Set<Long> convertToEntityAttribute(String dbData) {
    if (StringUtils.isBlank(dbData)) {
      return new LinkedHashSet<>();
    }
    String[] split = StringUtils.split(dbData, ",");
    Set<Long> res = new LinkedHashSet<>(Math.max((int) (split.length / 0.75F) + 1, 16));
    for (String s : split) {
      res.add(Long.valueOf(s));
    }
    return res;
  }
}
