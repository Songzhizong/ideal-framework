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
package cn.idealframework.event.condition;

import cn.idealframework.lang.StringUtils;
import lombok.*;
import lombok.extern.apachecommons.CommonsLog;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

/**
 * @author 宋志宗 on 2021/4/26
 */
@CommonsLog
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor
@AllArgsConstructor
public class Item {
  private String property;
  private Operator operator;
  private Object value;

  @Nonnull
  public static Item parse(@Nonnull String itemExpression) {
    Operator operator = null;
    int index = -1;
    char[] chars = itemExpression.toCharArray();
    for (int i = 0; i < chars.length; i++) {
      char c = chars[i];
      if (c == '>') {
        operator = Operator.GREATER;
        index = i;
        break;
      }
      if (c == '<') {
        operator = Operator.LESS;
        index = i;
        break;
      }
      if (c == '=') {
        operator = Operator.EQUAL;
        index = i;
        break;
      }
      if (c == '^') {
        operator = Operator.IN;
        index = i;
        break;
      }
    }
    if (index < 0) {
      throw new IllegalArgumentException("Conditional expressions lack operators");
    }
    if (index == 0) {
      throw new IllegalArgumentException("Conditional expressions cannot begin with an operator");
    }
    String key = StringUtils.substring(itemExpression, 0, index);
    String value = StringUtils.substring(itemExpression, index + 1);
    if (StringUtils.isBlank(key)) {
      throw new IllegalArgumentException("Conditional expressions lack property");
    }
    if (StringUtils.isBlank(value)) {
      throw new IllegalArgumentException("Conditional expressions lack value");
    }
    if (operator == Operator.GREATER || operator == Operator.LESS) {
      long longValue = Long.parseLong(value);
      return new Item(key, operator, longValue);
    }
    if (operator == Operator.EQUAL) {
      return new Item(key, operator, value);
    }
    //noinspection ConstantConditions
    if (operator == Operator.IN) {
      String[] values = StringUtils.split(value, ",");
      return new Item(key, operator, values);
    }
    throw new RuntimeException("未知操作符: " + operator.name());
  }

  @Nonnull
  public String toExpression() {
    String operatorValue;
    String valueValue;
    if (operator == Operator.GREATER) {
      valueValue = value.toString();
      operatorValue = ">";
    } else if (operator == Operator.LESS) {
      valueValue = value.toString();
      operatorValue = "<";
    } else if (operator == Operator.EQUAL) {
      valueValue = value.toString();
      operatorValue = "=";
    } else {
      operatorValue = "^";
      String[] value = (String[]) this.value;
      valueValue = String.join(",", value);
    }
    return property + operatorValue + valueValue;
  }

  public boolean match(@Nonnull Set<String> values) {
    if (values.isEmpty()) {
      return false;
    }
    if (operator == Operator.GREATER || operator == Operator.LESS) {
      boolean flag = false;
      for (String value : values) {
        long condition = ((Number) this.getValue()).longValue();
        long target;
        try {
          target = Long.parseLong(value);
        } catch (NumberFormatException e) {
          continue;
        }
        if (operator == Operator.GREATER && target > condition) {
          flag = true;
        }
        if (operator == Operator.LESS && target < condition) {
          flag = true;
        }
      }
      if (!flag) {
        return false;
      }
    }
    if (operator == Operator.EQUAL) {
      String condition = (String) this.getValue();
      if (!values.contains(condition)) {
        return false;
      }
    }
    if (operator == Operator.IN) {
      String[] condition = (String[]) this.value;
      boolean flag = false;
      for (String s : condition) {
        if (values.contains(s)) {
          flag = true;
          break;
        }
      }
      //noinspection RedundantIfStatement
      if (!flag) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Item item = (Item) o;
    boolean a = property.equals(item.property);
    boolean b = operator == item.operator;
    Object itemValue = item.value;
    boolean c;
    if (value == itemValue) {
      c = true;
    } else if (value instanceof Object[] && itemValue instanceof Object[]) {
      c = Arrays.equals((Object[]) value, (Object[]) itemValue);
    } else {
      c = value.equals(itemValue);
    }
    return a && b && c;
  }

  @Override
  public int hashCode() {
    return Objects.hash(property, operator, value);
  }

  protected enum Operator {
    /** 大于 */
    GREATER,
    /** 小于 */
    LESS,
    /** 等于 */
    EQUAL,
    /** in */
    IN,
  }
}
