/*
 * Copyright 2012-2015 Ray Holder
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.idealframework.util.retry;

import javax.annotation.Nonnull;
import java.util.concurrent.*;

/**
 * Factory class for instances of {@link AttemptTimeLimiter}
 *
 * @author Jason Dunkelberger (dirkraft)
 */
public class AttemptTimeLimiters {

  private AttemptTimeLimiters() {
  }

  /**
   * 单次尝试没有时间限制
   *
   * @param <V> The type of the computation result.
   * @return an {@link AttemptTimeLimiter} impl which has no time limit
   */
  @Nonnull
  public static <V> AttemptTimeLimiter<V> noTimeLimit() {
    return new NoAttemptTimeLimit<>();
  }

  /**
   * 为单次尝试固定超时时间
   *
   * @param duration        时间值
   * @param timeUnit        时间单位
   * @param executorService 执行操作的线程池
   * @param <V>             返回结果泛型
   * @return AttemptTimeLimiter
   */
  @Nonnull
  public static <V> AttemptTimeLimiter<V> fixedTimeLimit(long duration, @Nonnull TimeUnit timeUnit,
                                                         @Nonnull ExecutorService executorService) {
    return new FixedAttemptTimeLimit<>(duration, timeUnit, executorService);
  }

  //-------------------------------------------------------------------------
  // inner classes

  private static final class NoAttemptTimeLimit<V> implements AttemptTimeLimiter<V> {
    @Override
    public V call(@Nonnull Callable<V> callable) throws Exception {
      return callable.call();
    }
  }

  private static final class FixedAttemptTimeLimit<V> implements AttemptTimeLimiter<V> {

    private final long duration;
    private final TimeUnit timeUnit;
    private final ExecutorService executorService;

    private FixedAttemptTimeLimit(long duration, TimeUnit timeUnit, ExecutorService executorService) {
      Preconditions.assertNotNull(timeUnit, "timeUnit may not null");
      Preconditions.assertNotNull(executorService, "executorService may not null");
      this.duration = duration;
      this.timeUnit = timeUnit;
      this.executorService = executorService;
    }

    @Override
    public V call(Callable<V> callable) throws Exception {
      Preconditions.assertNotNull(callable);
      Preconditions.assertNotNull(timeUnit);
      Preconditions.checkArgument(duration > 0, "timeout must be positive: " + duration);

      Future<V> future = executorService.submit(callable);

      try {
        return future.get(duration, timeUnit);
      } catch (InterruptedException | TimeoutException e) {
        // mayInterruptIfRunning
        future.cancel(true);
        throw e;
      }
    }
  }

}
