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
package cn.idealframework.cache.impl;

import cn.idealframework.cache.Counter;
import com.github.benmanes.caffeine.cache.Caffeine;

import static org.junit.Assert.*;

/**
 * @author 宋志宗 on 2021/12/10
 */
public class AtomicCounterTest {
  private static final String KEY = "TEST";


  @org.junit.Test
  public void get() {
    Counter counter = new AtomicCounter(Caffeine.newBuilder().build());
    assertNull(counter.get(KEY));
    assertNull(counter.getAndSet(KEY, 20));
    Long aLong = counter.get(KEY);
    assertNotNull(aLong);
    assertEquals(aLong.longValue(), 20L);
  }

  @org.junit.Test
  public void getAndSet() {
    Counter counter = new AtomicCounter(Caffeine.newBuilder().build());
    assertNull(counter.getAndSet(KEY, 1000));
    Long set = counter.getAndSet(KEY, 2000);
    assertNotNull(set);
    assertEquals(set.longValue(), 1000);
  }

  @org.junit.Test
  public void getAndRemove() {
    Counter counter = new AtomicCounter(Caffeine.newBuilder().build());
    assertEquals(counter.increment(KEY, 1000), 1000);
    Long aLong = counter.getAndRemove(KEY);
    assertNotNull(aLong);
    assertEquals(aLong.longValue(), 1000);
    assertNull(counter.get(KEY));
  }

  @org.junit.Test
  public void remove() {
    Counter counter = new AtomicCounter(Caffeine.newBuilder().build());
    assertEquals(counter.increment(KEY, 1000), 1000);
    counter.remove(KEY);
    assertNull(counter.get(KEY));
  }

  @org.junit.Test
  public void increment() {
    Counter counter = new AtomicCounter(Caffeine.newBuilder().build());
    assertEquals(counter.increment(KEY, 1000), 1000);
    assertEquals(counter.increment(KEY, 1000), 2000);
  }

  @org.junit.Test
  public void decrement() {
    Counter counter = new AtomicCounter(Caffeine.newBuilder().build());
    assertEquals(counter.increment(KEY, 1000), 1000);
    assertEquals(counter.decrement(KEY, 500), 500);
  }
}
