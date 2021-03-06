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

import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 宋志宗 on 2021/1/19
 */
@Getter
@Setter
public class TestTreeNode implements TreeNode {
  @Nonnull
  private String id;
  @Nullable
  private String parentId;
  private List<TestTreeNode> childList = new ArrayList<>();

  public TestTreeNode(@Nonnull String id, @Nullable String parentId) {
    this.id = id;
    this.parentId = parentId;
  }

  @Nonnull
  @Override
  public Object getNodeId() {
    return id;
  }

  @Nullable
  @Override
  public Object getParentNodeId() {
    return parentId;
  }

  @Nonnull
  @Override
  public List<TestTreeNode> getChildNodes() {
    return childList;
  }
}
