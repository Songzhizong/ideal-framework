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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.apachecommons.CommonsLog;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 宋志宗 on 2021/4/26
 */
@CommonsLog
@Getter
@Setter(AccessLevel.PROTECTED)
public class EventConditionImpl implements EventCondition {
  private List<Group> groups = new ArrayList<>();

  protected EventConditionImpl() {
  }


  @Override
  public boolean match(@Nullable EventHeaders headers) {
    if (groups == null || groups.isEmpty()) {
      return true;
    }
    if (headers == null || headers.isEmpty()) {
      log.debug("Failed match. Event headers is empty");
      return false;
    }
    for (Group group : groups) {
      Set<Item> items = group.getItems();
      boolean flag = true;
      for (Item item : items) {
        String property = item.getProperty();
        Set<Object> values = headers.get(property);
        if (values == null || !item.match(values)) {
          flag = false;
        }
      }
      if (flag) {
        return true;
      }
    }
    return false;
  }

  @Nonnull
  @Override
  public String toExpression() {
    StringBuilder stringBuilder = new StringBuilder();
    List<Group> groups = this.getGroups();
    for (int i = 0; i < groups.size(); i++) {
      StringBuilder sb = new StringBuilder();
      Group group = groups.get(i);
      Set<Item> items = group.getItems();
      String itemExpression = items.stream()
          .map(Item::toExpression)
          .collect(Collectors.joining("&"));
      sb.append(itemExpression);
      if (i == groups.size() - 1) {
        stringBuilder.append(sb);
      } else {
        stringBuilder.append(sb).append("|");
      }
    }
    return stringBuilder.toString();
  }

  @Nonnull
  public PropertyConfigurer property(@Nonnull String property) {
    Group currentGroup = new Group();
    this.addGroup(currentGroup);
    return new PropertyConfigurer(this, currentGroup, property);
  }

  private void addGroup(Group group) {
    this.getGroups().add(group);
  }

  @Getter
  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  public static class PropertyConfigurer {
    private final EventConditionImpl condition;
    private final Group currentGroup;
    private final String property;

    @Nonnull
    public ItemConfigurer greaterThen(@Nonnull Number value) {
      return new ItemConfigurer(condition, currentGroup, new Item(property, Item.Operator.GREATER, value));
    }

    @Nonnull
    public ItemConfigurer lessThen(@Nonnull Number value) {
      return new ItemConfigurer(condition, currentGroup, new Item(property, Item.Operator.LESS, value));
    }

    @Nonnull
    public ItemConfigurer equalTo(@Nonnull String value) {
      return new ItemConfigurer(condition, currentGroup, new Item(property, Item.Operator.EQUAL, value));
    }

    @Nonnull
    public ItemConfigurer in(@Nonnull String... values) {
      return new ItemConfigurer(condition, currentGroup, new Item(property, Item.Operator.IN, values));
    }
  }

  public static class ItemConfigurer {
    private final EventConditionImpl condition;
    private final Group currentGroup;

    private ItemConfigurer(EventConditionImpl condition, Group currentGroup, Item currentItem) {
      this.condition = condition;
      this.currentGroup = currentGroup;
      this.currentGroup.addItem(currentItem);
    }


    @Nonnull
    public EventCondition end() {
      return this.condition;
    }

    @Nonnull
    public PropertyConfigurer property(String property) {
      return new PropertyConfigurer(condition, currentGroup, property);
    }

    @Nonnull
    public EventConditionImpl or() {
      return condition;
    }
  }
}
