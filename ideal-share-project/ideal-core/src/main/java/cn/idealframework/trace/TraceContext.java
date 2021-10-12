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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nonnull;
import java.beans.Transient;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 宋志宗 on 2021/5/31
 */
@SuppressWarnings("unused")
@Setter(AccessLevel.PRIVATE)
public class TraceContext {
  @Getter
  @Nonnull
  private final TraceInfo traceInfo = new TraceInfo();
  @Getter
  private final long createMillis = System.currentTimeMillis();
  private transient final AtomicInteger spanIdGenerator = new AtomicInteger(0);
  @Nonnull
  private final ConcurrentMap<String, Object> attributeMap = new ConcurrentHashMap<>();
  /** 唯一id */
  @Getter
  @Nonnull
  private final String traceId;

  /** 类型: http/bus/kafka... */
  @Getter
  @Nonnull
  private final String mode;

  /** 追踪标识, 由请求方定义 */
  @Getter
  @Nonnull
  private final String spanId;

  private transient final String logPrefix;

  public TraceContext(@Nonnull String mode,
                      @Nonnull String traceId,
                      @Nonnull String spanId) {
    this.traceId = traceId;
    this.mode = mode;
    this.spanId = spanId;
    this.traceInfo.setTraceId(traceId);
    this.traceInfo.setSpanId(spanId);
    this.traceInfo.setMode(mode);
    this.logPrefix = mode + ":" + traceId + ":" + spanId + " | ";
  }

  public String generateNextSpanId() {
    int increment = spanIdGenerator.incrementAndGet();
    return spanId + "-" + increment;
  }

  public <T> void putAttribute(@Nonnull String attributeName, @Nonnull T attributeValue) {
    attributeMap.put(attributeName, attributeValue);
  }

  @Nonnull
  public <T> Optional<T> getAttribute(@Nonnull String attributeName) {
    Object object = attributeMap.get(attributeName);
    if (object == null) {
      return Optional.empty();
    }
    @SuppressWarnings("unchecked")
    T t = (T) object;
    return Optional.of(t);
  }

  @Nonnull
  public Map<String, Object> getAttributes() {
    return attributeMap;
  }

  public long getSurvivalMillis() {
    return System.currentTimeMillis() - createMillis;
  }

  @Transient
  public String getLogPrefix() {
    return logPrefix;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TraceContext that = (TraceContext) o;
    return traceId.equals(that.traceId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(traceId);
  }

}
