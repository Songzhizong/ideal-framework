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

import javax.annotation.Nonnull;

/**
 * id生成器工厂接口
 *
 * @author 宋志宗 on 2020/10/10 4:21 下午
 */
public interface IDGeneratorFactory {

  /**
   * 根据业务类型获取id生成器
   *
   * @param biz 业务类型
   * @return id生成器
   */
  @Nonnull
  IDGenerator getGenerator(@Nonnull String biz);

  /**
   * 释放资源, 例如程序关闭时snowflake从注册中心释放占用的机器码
   */
  void release();
}
