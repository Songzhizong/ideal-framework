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

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author 宋志宗 on 2021/6/4
 */
public class TreeNodeTest {

  @Test
  public void toTreeList() {
    List<TestTreeNode> list = new ArrayList<>();
    list.add(new TestTreeNode("1", "-1")); // root
    list.add(new TestTreeNode("2", "-1")); // root
    list.add(new TestTreeNode("3", "1"));
    list.add(new TestTreeNode("4", "1"));
    list.add(new TestTreeNode("5", "2"));
    list.add(new TestTreeNode("6", "3"));
    list.add(new TestTreeNode("7", null)); // root
    list.add(new TestTreeNode("8", "5"));
    list.add(new TestTreeNode("9", "11")); // root
    List<TestTreeNode> result = TreeNode.toTreeList(list);
    assertEquals(result.size(), 4);
  }

}
