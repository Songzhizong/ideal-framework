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

import cn.idealframework.event.message.EventHeaders;
import cn.idealframework.lang.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

/**
 * @author 宋志宗 on 2021/4/26
 */
public interface EventCondition {

  boolean match(@Nullable EventHeaders headers);

  @Nonnull
  static EventConditionImpl create() {
    return new EventConditionImpl();
  }


  static EventCondition empty() {
    return EmptyEventCondition.EMPTY_EVENT_CONDITION;
  }

  @Nonnull
  static EventCondition parse(@Nullable String conditionExpression) {
    if (StringUtils.isBlank(conditionExpression)) {
      return empty();
    }
    EventConditionImpl condition = EventCondition.create();
    String[] groupsExpressions = StringUtils.split(conditionExpression, "|");
    for (String groupsExpression : groupsExpressions) {
      Group group = new Group();
      Set<Item> items = group.getItems();
      String[] itemExpressions = StringUtils.split(groupsExpression, "&");
      for (String itemExpression : itemExpressions) {
        items.add(Item.parse(itemExpression));
      }
      condition.getGroups().add(group);
    }
    return condition;
  }

  @Nonnull
  String toExpression();
}
