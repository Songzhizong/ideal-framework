package cn.idealframework.util.retry.predicate;

import cn.idealframework.util.retry.attempt.Attempt;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

/**
 * Created on 2018/2/11
 *
 * @author lowzj
 */
public class ResultPredicate<V> implements Predicate<Attempt<V>> {

  private final Predicate<V> delegate;

  public ResultPredicate(Predicate<V> delegate) {
    this.delegate = delegate;
  }

  @Override
  public boolean test(@Nonnull Attempt<V> attempt) {
    if (!attempt.hasResult()) {
      return false;
    }
    V result = attempt.getResult();
    return delegate.test(result);
  }
}
