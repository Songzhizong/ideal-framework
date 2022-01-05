package cn.idealframework.util.retry;

import org.junit.Test;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

/**
 * @author 宋志宗 on 2022/1/5
 */
public class AsyncRetryerTest {

  @Test
  public void call() throws InterruptedException, ExecutionException {
    AsyncRetryer<Integer> retryer = RetryerBuilder.<Integer>newBuilder()
      .retryIfException()
      .retryIfResult(Objects::isNull)
      .withRetryListener(attempt -> {
        long attemptNumber = attempt.getAttemptNumber();
        System.out.println("第" + attemptNumber + "次重试");
      })
      .withWaitStrategy(WaitStrategies.fixedWait(Duration.ofSeconds(5)))
      .withStopStrategy(StopStrategies.stopAfterAttempt(5))
      .buildAsyncRetryer(Executors.newFixedThreadPool(10));

    CompletableFuture<Integer> call = retryer.call(() -> {
      System.out.println("开始执行");
      return 1;
    });
    System.out.println(call.get());
  }
}
