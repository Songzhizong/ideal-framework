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
package cn.idealframework.trace;

import lombok.RequiredArgsConstructor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author 宋志宗 on 2021/5/31
 */
@SuppressWarnings("unused")
public class TraceableThreadPoolExecutor extends ThreadPoolExecutor {

  public TraceableThreadPoolExecutor(int corePoolSize,
                                     int maximumPoolSize,
                                     long keepAliveTime,
                                     @Nonnull TimeUnit unit,
                                     @Nonnull BlockingQueue<Runnable> workQueue) {
    super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
  }

  public TraceableThreadPoolExecutor(int corePoolSize,
                                     int maximumPoolSize,
                                     long keepAliveTime,
                                     @Nonnull TimeUnit unit,
                                     @Nonnull BlockingQueue<Runnable> workQueue,
                                     @Nonnull ThreadFactory threadFactory) {
    super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
  }

  public TraceableThreadPoolExecutor(int corePoolSize,
                                     int maximumPoolSize,
                                     long keepAliveTime,
                                     @Nonnull TimeUnit unit,
                                     @Nonnull BlockingQueue<Runnable> workQueue,
                                     @Nonnull RejectedExecutionHandler handler) {
    super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
  }

  public TraceableThreadPoolExecutor(int corePoolSize,
                                     int maximumPoolSize,
                                     long keepAliveTime,
                                     @Nonnull TimeUnit unit,
                                     @Nonnull BlockingQueue<Runnable> workQueue,
                                     @Nonnull ThreadFactory threadFactory,
                                     @Nonnull RejectedExecutionHandler handler) {
    super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
  }

  @Override
  public void execute(@Nonnull Runnable command) {
    TraceContext context = TraceContextHolder.current().orElse(null);
    TraceableRunnable runnable = new TraceableRunnable(command, context);
    super.execute(runnable);
  }

  @Override
  protected <T> RunnableFuture<T> newTaskFor(@Nonnull Runnable runnable, T value) {
    TraceContext context = TraceContextHolder.current().orElse(null);
    TraceableRunnable traceableRunnable = new TraceableRunnable(runnable, context);
    return super.newTaskFor(traceableRunnable, value);
  }

  @Override
  protected <T> RunnableFuture<T> newTaskFor(@Nonnull Callable<T> callable) {
    TraceContext context = TraceContextHolder.current().orElse(null);
    TraceableCallable<T> traceableCallable = new TraceableCallable<>(callable, context);
    return super.newTaskFor(traceableCallable);
  }

  @Override
  public Future<?> submit(@Nonnull Runnable task) {
    TraceContext context = TraceContextHolder.current().orElse(null);
    TraceableRunnable runnable = new TraceableRunnable(task, context);
    return super.submit(runnable);
  }

  @Override
  public <T> Future<T> submit(@Nonnull Runnable task, T result) {
    TraceContext context = TraceContextHolder.current().orElse(null);
    TraceableRunnable runnable = new TraceableRunnable(task, context);
    return super.submit(runnable, result);
  }

  @Override
  public <T> Future<T> submit(@Nonnull Callable<T> task) {
    TraceContext context = TraceContextHolder.current().orElse(null);
    TraceableCallable<T> callable = new TraceableCallable<>(task, context);
    return super.submit(callable);
  }

  @Override
  public <T> T invokeAny(@Nonnull Collection<? extends Callable<T>> tasks)
    throws InterruptedException, ExecutionException {
    List<TraceableCallable<T>> callables = generateTraceableCallables(tasks);
    return super.invokeAny(callables);
  }

  @Override
  public <T> T invokeAny(@Nonnull Collection<? extends Callable<T>> tasks,
                         long timeout,
                         @Nonnull TimeUnit unit)
    throws InterruptedException, ExecutionException, TimeoutException {
    List<TraceableCallable<T>> callables = generateTraceableCallables(tasks);
    return super.invokeAny(callables, timeout, unit);
  }

  @Override
  public <T> List<Future<T>> invokeAll(@Nonnull Collection<? extends Callable<T>> tasks)
    throws InterruptedException {
    List<TraceableCallable<T>> callables = generateTraceableCallables(tasks);
    return super.invokeAll(callables);
  }

  @Override
  public <T> List<Future<T>> invokeAll(@Nonnull Collection<? extends Callable<T>> tasks,
                                       long timeout,
                                       @Nonnull TimeUnit unit)
    throws InterruptedException {
    List<TraceableCallable<T>> callables = generateTraceableCallables(tasks);
    return super.invokeAll(callables, timeout, unit);
  }

  private <T> List<TraceableCallable<T>> generateTraceableCallables(Collection<? extends Callable<T>> tasks) {
    if (tasks == null) {
      throw new NullPointerException("tasks is null");
    }
    TraceContext context = TraceContextHolder.current().orElse(null);
    return tasks.stream()
        .map(task -> new TraceableCallable<>(task, context))
        .collect(Collectors.toList());
  }


  @RequiredArgsConstructor
  public static class TraceableRunnable implements Runnable {
    @Nonnull
    private final Runnable runnable;
    @Nullable
    private final TraceContext traceContext;

    @Override
    public void run() {
      if (traceContext != null) {
        TraceContextHolder.init(traceContext);
      }
      try {
        runnable.run();
      } finally {
        TraceContextHolder.release();
      }
    }
  }

  @RequiredArgsConstructor
  public static class TraceableCallable<V> implements Callable<V> {
    @Nonnull
    private final Callable<V> callable;
    @Nullable
    private final TraceContext traceContext;

    @Override
    public V call() throws Exception {
      if (traceContext != null) {
        TraceContextHolder.init(traceContext);
      }
      try {
        return callable.call();
      } finally {
        TraceContextHolder.release();
      }
    }
  }
}
