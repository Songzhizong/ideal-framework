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
package cn.idealframework.date;

import org.junit.Test;

import java.text.ParseException;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * @author 宋志宗 on 2021/8/12
 */
public class SafetyDateFormatTest {
  @Test
  public void test() throws InterruptedException {
    SafetyDateFormat format = new SafetyDateFormat(DateTimes.YYYY_MM_DD_HH_MM_SS_SSS);
    int count = 16;
    CountDownLatch countDownLatch = new CountDownLatch(count);
    Date date = new Date();
    String format1 = format.format(date);
    for (int i = 0; i < count; i++) {
      Thread thread = new Thread(() -> {
        try {
          for (int i1 = 0; i1 < 1000; i1++) {
            format.parse(format1);
          }
        } catch (ParseException e) {
          System.err.println(e.getMessage());
        } finally {
          countDownLatch.countDown();
        }
      });
      thread.start();
    }
    countDownLatch.await();
  }
}