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
import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author 宋志宗 on 2021/12/10
 */
@RequiredArgsConstructor
public class AtomicCounter implements Counter {
  private final Cache<String, AtomicLong> cache;

  @Nullable
  @Override
  public Long get(@Nonnull String key) {
    AtomicLong atomicLong = cache.getIfPresent(key);
    if (atomicLong == null) {
      return null;
    }
    return atomicLong.get();
  }

  @Nullable
  @Override
  public Long getAndSet(@Nonnull String key, long value) {
    AtomicBoolean flag = new AtomicBoolean(false);
    AtomicLong atomicLong = cache.get(key, k -> {
      flag.set(true);
      return new AtomicLong(0);
    });
    assert atomicLong != null;
    long oldValue = atomicLong.getAndAdd(value);
    if (flag.get()) {
      return null;
    }
    return oldValue;
  }

  @Nullable
  @Override
  public Long getAndRemove(@Nonnull String key) {
    AtomicLong atomicLong = cache.getIfPresent(key);
    if (atomicLong == null) {
      return null;
    }
    cache.invalidate(key);
    return atomicLong.get();
  }

  @Override
  public void remove(@Nonnull String key) {
    cache.invalidate(key);
  }

  @Override
  public long increment(@Nonnull String key, long delta) {
    AtomicLong atomicLong = cache.get(key, k -> new AtomicLong(0));
    assert atomicLong != null;
    return atomicLong.addAndGet(delta);
  }

  @Override
  public long decrement(@Nonnull String key, long delta) {
    AtomicLong atomicLong = cache.get(key, k -> new AtomicLong(0));
    assert atomicLong != null;
    return atomicLong.addAndGet(-delta);
  }
}
