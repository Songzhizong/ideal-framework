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
 * 负载均衡策略枚举
 *
 * @author 宋志宗 on 2020/8/20
 */
public enum LbStrategyEnum {
  /**
   * 一致性Hash
   */
  CONSISTENT_HASH("一致性Hash"),
  /**
   * 最不经常使用
   */
  LFU("最不经常使用"),
  /**
   * 最近最久未使用
   */
  LRU("最近最久未使用"),
  /**
   * 轮询
   */
  ROUND_ROBIN("轮询"),
  /**
   * 随机
   */
  RANDOM("随机"),
  /**
   * 加权轮询
   */
  WEIGHT_ROUND_ROBIN("加权轮询"),
  /**
   * 加权随机
   */
  WEIGHT_RANDOM("加权随机"),
  ;

  @Nonnull
  private final String desc;

  LbStrategyEnum(@Nonnull String desc) {
    this.desc = desc;
  }

  @Nonnull
  public String getDesc() {
    return desc;
  }
}
