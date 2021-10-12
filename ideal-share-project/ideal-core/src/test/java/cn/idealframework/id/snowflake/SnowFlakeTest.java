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
package cn.idealframework.id.snowflake;

import org.junit.Assert;

import static org.junit.Assert.assertNotEquals;

/**
 * @author 宋志宗 on 2021/6/1
 */
public class SnowFlakeTest {
  private static final SnowFlake snowFlake = new SnowFlake(0, 0);

  @org.junit.Test
  public void generate() {
    long generate = snowFlake.generate();
    assertNotEquals(generate, 0L);
    System.out.println(generate);
    int maxDataCenterNum = SnowFlake.MAX_DATA_CENTER_NUM;
    int maxMachineNum = SnowFlake.MAX_MACHINE_NUM;
    System.out.println("maxDataCenterNum = " + maxDataCenterNum);
    System.out.println("maxMachineNum = " + maxMachineNum);
  }

  @org.junit.Test
  public void generate1() {
    int count = (SnowFlake.MAX_SEQUENCE_NUM + 1) * 2000 + 1;
    long l = System.currentTimeMillis();
    for (int i = 0; i < count; i++) {
      snowFlake.generate();
    }
    long consuming = System.currentTimeMillis() - l;
    // 预期耗时大概是2秒, 即每秒生成1024个id
    System.out.println("生成" + count + "个id, 耗时: " + consuming + "ms");
    Assert.assertTrue(consuming >= 1900 && consuming <= 2100);
  }

  @org.junit.Test
  public void restoreTimestamp() {
  }

  @org.junit.Test
  public void generateMinValueByTimestamp() {
  }

}
