package cn.idealframework.operation;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * @author 宋志宗 on 2022/3/30
 */
public interface OperatorContextHolder {

  @Nonnull
  Optional<OperatorContext> current();
}
