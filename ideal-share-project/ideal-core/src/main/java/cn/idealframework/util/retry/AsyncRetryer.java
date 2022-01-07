package cn.idealframework.util.retry;

import cn.idealframework.concurrent.BasicThreadFactory;
import cn.idealframework.util.retry.attempt.Attempt;
import cn.idealframework.util.retry.attempt.ExceptionAttempt;
import cn.idealframework.util.retry.attempt.ResultAttempt;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.concurrent.*;
import java.util.function.Predicate;

/**
 * Created on 2018/2/11
 *
 * @author lowzj
 */
public class AsyncRetryer<V> {
  /** 延迟调度线程池, 该线程池负责全局延迟调度, 不参与计算. 计算任务在传入的 workerExecutor 中执行 */
  private static final ScheduledExecutorService SCHEDULED_EXECUTOR
    = new ScheduledThreadPoolExecutor(Math.max(Runtime.getRuntime().availableProcessors(), 8),
    BasicThreadFactory.builder().namingPattern("retry-scheduled-%d").build());
  private final StopStrategy stopStrategy;
  private final WaitStrategy waitStrategy;
  private final AttemptTimeLimiter<V> attemptTimeLimiter;
  private final Predicate<Attempt<V>> rejectionPredicate;
  private final Collection<RetryListener<V>> listeners;
  private final ExecutorService workerExecutor;

  static {
    Runtime.getRuntime().addShutdownHook(new Thread(AsyncRetryer.SCHEDULED_EXECUTOR::shutdown));
  }

  /**
   * Constructor
   *
   * @param attemptTimeLimiter to prevent from any single attempt from spinning infinitely
   * @param stopStrategy       the strategy used to decide when the retryer must stop retrying
   * @param waitStrategy       the strategy used to decide how much time to sleep between attempts
   * @param rejectionPredicate the predicate used to decide if the attempt must be rejected
   *                           or not. If an attempt is rejected, the retryer will retry the call, unless the stop
   *                           strategy indicates otherwise or the thread is interrupted.
   * @param listeners          collection of retry listeners
   * @param workerExecutor     to retry the call in thread pool
   */
  AsyncRetryer(AttemptTimeLimiter<V> attemptTimeLimiter,
               StopStrategy stopStrategy,
               WaitStrategy waitStrategy,
               Predicate<Attempt<V>> rejectionPredicate,
               Collection<RetryListener<V>> listeners,
               ExecutorService workerExecutor) {
    Preconditions.assertNotNull(attemptTimeLimiter, "timeLimiter may not be null");
    Preconditions.assertNotNull(stopStrategy, "stopStrategy may not be null");
    Preconditions.assertNotNull(waitStrategy, "waitStrategy may not be null");
    Preconditions.assertNotNull(rejectionPredicate, "rejectionPredicate may not be null");
    Preconditions.assertNotNull(listeners, "listeners may not be null");
    Preconditions.assertNotNull(workerExecutor, "workerExecutor may not be null");

    this.attemptTimeLimiter = attemptTimeLimiter;
    this.stopStrategy = stopStrategy;
    this.waitStrategy = waitStrategy;
    this.rejectionPredicate = rejectionPredicate;
    this.listeners = listeners;
    this.workerExecutor = workerExecutor;
  }

  public static void shutdown() {
    AsyncRetryer.SCHEDULED_EXECUTOR.shutdown();
  }

  public CompletableFuture<V> call(Callable<V> callable) {
    CompletableFuture<V> resultFuture = new CompletableFuture<>();
    workerExecutor.execute(createRunner(callable, System.nanoTime(), 1, resultFuture));
    return resultFuture;
  }

  @SuppressWarnings("DuplicatedCode")
  @Nonnull
  private Runnable createRunner(Callable<V> callable, long startTime, int attemptNumber,
                                CompletableFuture<V> resultFuture) {
    return () -> {
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
        try {
          V result = attempt.get();
          resultFuture.complete(result);
        } catch (ExecutionException e) {
          resultFuture.completeExceptionally(e);
        }
      }

      if (resultFuture.isDone()) {
        return;
      }
      if (stopStrategy.shouldStop(attempt)) {
        resultFuture.completeExceptionally(new RetryException(attemptNumber, attempt));
      } else {
        Runnable runner = createRunner(callable, startTime, attemptNumber + 1, resultFuture);
        long delayMills = waitStrategy.computeSleepTime(attempt);
        if (delayMills < 1) {
          workerExecutor.execute(runner);
        } else {
          SCHEDULED_EXECUTOR.schedule(() -> workerExecutor.execute(runner), delayMills, TimeUnit.MILLISECONDS);
        }
      }
    };
  }
}
