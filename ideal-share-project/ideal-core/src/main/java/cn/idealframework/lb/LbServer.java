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
package cn.idealframework.lb;

import javax.annotation.Nonnull;

/**
 * @author 宋志宗 on 2020/8/19
 */
public interface LbServer {

  /**
   * @return 唯一ID
   */
  @Nonnull
  String getInstanceId();

  /**
   * 获取权重
   * <p>如果需要使用加权策略, 请实现该方法</p>
   *
   * @return 权重, 用于加权算法, 至少为1
   */
  default int getWeight() {
    final String className = this.getClass().getName();
    throw new UnsupportedOperationException(className + " not implemented getWeight");
  }
}
