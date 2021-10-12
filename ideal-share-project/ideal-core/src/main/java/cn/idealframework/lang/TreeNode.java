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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.beans.Transient;
import java.util.*;

/**
 * 树节点接口
 *
 * @author 宋志宗 on 2021/1/19
 */
public interface TreeNode {

  @Nonnull
  static <E extends TreeNode> List<E> toTreeList(@Nonnull Collection<E> source) {
    int size = source.size();
    if (size < 2) {
      return new ArrayList<>(source);
    }
    List<E> result = new ArrayList<>();
    Map<Object, E> sourceMap = new HashMap<>(Math.max((int) (size / 0.75F) + 1, 16));
    for (E e : source) {
      if (e != null) {
        Object nodeId = e.getNodeId();
        sourceMap.put(nodeId, e);
      }
    }
    for (E e : source) {
      if (e == null) {
        continue;
      }
      Object parentNodeId = e.getParentNodeId();
      E node = null;
      if (parentNodeId != null) {
        node = sourceMap.get(parentNodeId);
      }
      if (node == null) {
        result.add(e);
      } else {
        @SuppressWarnings("rawtypes")
        List childNodes = node.getChildNodes();
        //noinspection unchecked
        childNodes.add(e);
      }
    }
    return result;
  }

  /**
   * @return 当前对象的父id
   */
  @Nullable
  @Transient
  Object getParentNodeId();

  /**
   * @return 当前对象的唯一id
   */
  @Nonnull
  @Transient
  Object getNodeId();

  /**
   * @return 存储子节点的已实例化可变list容器
   */
  @Nonnull
  @Transient
  List<? extends TreeNode> getChildNodes();
}
