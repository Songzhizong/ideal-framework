package cn.idealframework.util.retry.attempt;

import java.util.concurrent.ExecutionException;

/**
 * Created on 2018/2/11
 *
 * @author lowzj
 */
public class ResultAttempt<R> implements Attempt<R> {
  private final R result;
  private final long attemptNumber;
  private final long delaySinceFirstAttempt;

  public ResultAttempt(R result, long attemptNumber, long delaySinceFirstAttempt) {
    this.result = result;
    this.attemptNumber = attemptNumber;
    this.delaySinceFirstAttempt = delaySinceFirstAttempt;
  }

  @Override
  public R get() throws ExecutionException {
    return result;
  }

  @Override
  public boolean hasResult() {
    return true;
  }

  @Override
  public boolean hasException() {
    return false;
  }

  @Override
  public R getResult() throws IllegalStateException {
    return result;
  }

  @Override
  public Throwable getExceptionCause() throws IllegalStateException {
    throw new IllegalStateException("The attempt resulted in a result, not in an exception");
  }

  @Override
  public long getAttemptNumber() {
    return attemptNumber;
  }

  @Override
  public long getDelaySinceFirstAttempt() {
    return delaySinceFirstAttempt;
  }
}
