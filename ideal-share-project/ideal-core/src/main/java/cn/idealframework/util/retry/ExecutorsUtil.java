package cn.idealframework.util.retry;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * Created on 2018/2/11
 *
 * @author lowzj
 */
public final class ExecutorsUtil {
  private static final Map<String, ExecutorService> executors = new ConcurrentHashMap<>();

  public static ScheduledExecutorService scheduledExecutorService(String poolName, int corePoolSize) {
    return (ScheduledExecutorService) getOrCreate(poolName, ScheduledExecutorService.class,
      () -> new ScheduledThreadPoolExecutor(corePoolSize, newThreadFactor(poolName)));
  }

  public static ExecutorService executorService(String poolName, int corePoolSize, int maximumPoolSize) {
    return getOrCreate(poolName, ExecutorService.class,
      () -> new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
        60L, TimeUnit.SECONDS,
        new LinkedBlockingQueue<>(), newThreadFactor(poolName)));
  }

  //-------------------------------------------------------------------------
  // private methods

  private static ExecutorService getOrCreate(String poolName, Class<?> returnClass,
                                             Supplier<? extends ExecutorService> executorServiceSupplier) {
    Preconditions.assertNotNull(poolName, "poolName may not be null");
    String key = key(poolName, returnClass);

    ExecutorService executorService = executors.get(key);
    if (executorService != null) {
      return executorService;
    }
    return executors.computeIfAbsent(key, k -> executorServiceSupplier.get());
  }

  @Nonnull
  private static ThreadFactory newThreadFactor(String name) {
    return new NamedThreadFactory(name);
  }

  @Nonnull
  private static String key(@Nonnull String poolName, @Nonnull Class<?> returnClass) {
    return poolName + "#" + returnClass.getSimpleName();
  }

  static class NamedThreadFactory implements ThreadFactory {
    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;

    NamedThreadFactory(String namePrefix) {
      Preconditions.assertNotNull(namePrefix, "namePrefix may not be null");
      SecurityManager s = System.getSecurityManager();
      group = (s != null) ? s.getThreadGroup() :
        Thread.currentThread().getThreadGroup();
      this.namePrefix = namePrefix;
    }

    @Override
    public Thread newThread(@Nonnull Runnable r) {
      Thread t = new Thread(group, r,
        namePrefix + threadNumber.getAndIncrement(),
        0);
      if (t.isDaemon()) {
        t.setDaemon(false);
      }
      if (t.getPriority() != Thread.NORM_PRIORITY) {
        t.setPriority(Thread.NORM_PRIORITY);
      }
      return t;
    }
  }
}
