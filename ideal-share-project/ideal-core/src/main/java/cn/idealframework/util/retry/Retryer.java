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

import cn.idealframework.util.retry.attempt.Attempt;
import cn.idealframework.util.retry.attempt.ExceptionAttempt;
import cn.idealframework.util.retry.attempt.ResultAttempt;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

/**
 * A retryer, which executes a call, and retries it until it succeeds, or
 * a stop strategy decides to stop retrying. A wait strategy is used to sleep
 * between attempts. The strategy to decide if the call succeeds or not is
 * also configurable.
 * <br><br>
 * A retryer can also wrap the callable into a RetryerCallable, which can be submitted to an executor.
 * <br><br>
 * Retryer instances are better constructed with a {@link RetryerBuilder}. A retryer
 * is thread-safe, provided the arguments passed to its constructor are thread-safe.
 *
 * @param <V> the type of the call return value
 * @author JB
 * @author Jason Dunkelberger (dirkraft)
 */
public final class Retryer<V> {
  private final StopStrategy stopStrategy;
  private final WaitStrategy waitStrategy;
  private final BlockStrategy blockStrategy;
  private final AttemptTimeLimiter<V> attemptTimeLimiter;
  private final Predicate<Attempt<V>> rejectionPredicate;
  private final Collection<RetryListener<V>> listeners;

  /**
   * Constructor
   *
   * @param attemptTimeLimiter to prevent from any single attempt from spinning infinitely
   * @param stopStrategy       the strategy used to decide when the retryer must stop retrying
   * @param waitStrategy       the strategy used to decide how much time to sleep between attempts
   * @param blockStrategy      the strategy used to decide how to block between retry attempts; eg, Thread#sleep(), latches, etc.
   * @param rejectionPredicate the predicate used to decide if the attempt must be rejected
   *                           or not. If an attempt is rejected, the retryer will retry the call, unless the stop
   *                           strategy indicates otherwise or the thread is interrupted.
   * @param listeners          collection of retry listeners
   */
  Retryer(AttemptTimeLimiter<V> attemptTimeLimiter,
          StopStrategy stopStrategy,
          WaitStrategy waitStrategy,
          BlockStrategy blockStrategy,
          Predicate<Attempt<V>> rejectionPredicate,
          Collection<RetryListener<V>> listeners) {
    Preconditions.assertNotNull(attemptTimeLimiter, "timeLimiter may not be null");
    Preconditions.assertNotNull(stopStrategy, "stopStrategy may not be null");
    Preconditions.assertNotNull(waitStrategy, "waitStrategy may not be null");
    Preconditions.assertNotNull(blockStrategy, "blockStrategy may not be null");
    Preconditions.assertNotNull(rejectionPredicate, "rejectionPredicate may not be null");
    Preconditions.assertNotNull(listeners, "listeners may not null");

    this.attemptTimeLimiter = attemptTimeLimiter;
    this.stopStrategy = stopStrategy;
    this.waitStrategy = waitStrategy;
    this.blockStrategy = blockStrategy;
    this.rejectionPredicate = rejectionPredicate;
    this.listeners = listeners;
  }

  /**
   * Executes the given callable. If the rejection predicate
   * accepts the attempt, the stop strategy is used to decide if a new attempt
   * must be made. Then the wait strategy is used to decide how much time to sleep
   * and a new attempt is made.
   *
   * @param callable the callable task to be executed
   * @return the computed result of the given callable
   * @throws ExecutionException if the given callable throws an exception, and the
   *                            rejection predicate considers the attempt as successful. The original exception
   *                            is wrapped into an ExecutionException.
   * @throws RetryException     if all the attempts failed before the stop strategy decided
   *                            to abort, or the thread was interrupted. Note that if the thread is interrupted,
   *                            this exception is thrown and the thread's interrupt status is set.
   */
  @SuppressWarnings("DuplicatedCode")
  public V call(Callable<V> callable) throws ExecutionException, RetryException {
    long startTime = System.nanoTime();
    for (int attemptNumber = 1; ; attemptNumber++) {
      Attempt<V> attempt;
      try {
        V result = attemptTimeLimiter.call(callable);
        attempt = new ResultAttempt<>(result, attemptNumber,
          TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime));
      } catch (Throwable t) {
        attempt = new ExceptionAttempt<>(t, attemptNumber,
          TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime));
      }

      for (RetryListener<V> listener : listeners) {
        listener.onRetry(attempt);
      }

      if (!rejectionPredicate.test(attempt)) {
        return attempt.get();
      }
      if (stopStrategy.shouldStop(attempt)) {
        throw new RetryException(attemptNumber, attempt);
      } else {
        long sleepTime = waitStrategy.computeSleepTime(attempt);
        try {
          blockStrategy.block(sleepTime);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          throw new RetryException(attemptNumber, attempt);
        }
      }
    }
  }

  /**
   * Wraps the given {@link Callable} in a {@link RetryerCallable}, which can
   * be submitted to an executor. The returned {@link RetryerCallable} uses
   * this {@link Retryer} instance to call the given {@link Callable}.
   *
   * @param callable the callable to wrap
   * @return a {@link RetryerCallable} that behaves like the given {@link Callable} with retry behavior defined by this {@link Retryer}
   */
  @Nonnull
  public RetryerCallable<V> wrap(@Nonnull Callable<V> callable) {
    return new RetryerCallable<>(this, callable);
  }


  /**
   * A {@link Callable} which wraps another {@link Callable} in order to add
   * retrying behavior from a given {@link Retryer} instance.
   *
   * @author JB
   */
  public static class RetryerCallable<X> implements Callable<X> {
    private final Retryer<X> retryer;
    private final Callable<X> callable;

    private RetryerCallable(Retryer<X> retryer,
                            Callable<X> callable) {
      this.retryer = retryer;
      this.callable = callable;
    }

    /**
     * Makes the enclosing retryer call the wrapped callable.
     *
     * @see Retryer#call(Callable)
     */
    @Override
    public X call() throws ExecutionException, RetryException {
      return retryer.call(callable);
    }
  }
}
