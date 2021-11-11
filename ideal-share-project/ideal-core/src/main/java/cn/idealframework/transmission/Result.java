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
package cn.idealframework.transmission;

import cn.idealframework.transmission.exception.VisibleException;
import cn.idealframework.transmission.exception.VisibleRuntimeException;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.beans.Transient;
import java.util.function.Function;

/**
 * @author 宋志宗 on 2021/8/24
 */
@Getter
@Setter
@Accessors(chain = true)
public class Result<T> extends BasicResult {
  private static final long serialVersionUID = -4328578094677050954L;

  @Nullable
  private T data;

  public Result() {
    super();
  }

  @Nonnull
  public static <T> Result<T> success() {
    Result<T> res = new Result<>();
    res.setSuccess(true);
    res.setCode(ResMsg.SUCCESS.code());
    res.setMessage(ResMsg.SUCCESS.message());
    return res;
  }

  @Nonnull
  public static <T> Result<T> create(boolean success, int code,
                                     @Nullable String message, @Nullable T data) {
    Result<T> res = new Result<>();
    res.setSuccess(success);
    res.setCode(code);
    res.setMessage(message);
    res.setData(data);
    return res;
  }

  @Nonnull
  public static <T> Result<T> success(@Nullable String message) {
    Result<T> res = new Result<>();
    res.setSuccess(true);
    res.setCode(ResMsg.SUCCESS.code());
    res.setMessage(message);
    return res;
  }

  @Nonnull
  public static <T> Result<T> success(int code, @Nullable String message) {
    Result<T> res = new Result<>();
    res.setSuccess(true);
    res.setCode(code);
    res.setMessage(message);
    return res;
  }

  @Nonnull
  public static <T> Result<T> success(@Nonnull ResMsg resMsg) {
    Result<T> res = new Result<>();
    res.setSuccess(true);
    res.setCode(resMsg.code());
    res.setMessage(resMsg.message());
    return res;
  }

  @Nonnull
  public static <T> Result<T> failure(int code, @Nullable String message) {
    Result<T> res = new Result<>();
    res.setSuccess(false);
    res.setCode(code);
    res.setMessage(message);
    return res;
  }

  @Nonnull
  public static <T> Result<T> failure(@Nonnull ResMsg resMsg) {
    Result<T> res = new Result<>();
    res.setSuccess(false);
    res.setCode(resMsg.code());
    res.setMessage(resMsg.message());
    return res;
  }

  @Nonnull
  public static <T> Result<T> data(@Nullable T data) {
    Result<T> res = new Result<>();
    res.setSuccess(true);
    res.setCode(ResMsg.SUCCESS.code());
    res.setMessage(ResMsg.SUCCESS.message());
    res.setData(data);
    return res;
  }

  @Nonnull
  public static <T> Result<T> data(@Nullable T data, @Nullable String message) {
    Result<T> res = new Result<>();
    res.setSuccess(true);
    res.setCode(ResMsg.SUCCESS.code());
    res.setMessage(message);
    res.setData(data);
    return res;
  }

  @Nonnull
  public static <T> Result<T> message(boolean success, int code, @Nullable String message) {
    Result<T> res = new Result<>();
    res.setSuccess(false);
    res.setCode(code);
    res.setMessage(message);
    return res;
  }

  @Nonnull
  public static <T> Result<T> exception(@Nonnull Throwable t) {
    if (t instanceof VisibleException) {
      return failure((VisibleException) t);
    }
    if (t instanceof VisibleRuntimeException) {
      return failure((VisibleRuntimeException) t);
    }
    Result<T> res = new Result<>();
    res.setSuccess(false);
    res.setCode(ResMsg.INTERNAL_SERVER_ERROR.code());
    res.setMessage(t.getMessage());
    return res;
  }

  @Nonnull
  public static <T> Result<T> exception(@Nullable String message) {
    Result<T> res = new Result<>();
    res.setSuccess(false);
    res.setCode(ResMsg.INTERNAL_SERVER_ERROR.code());
    res.setMessage(message);
    return res;
  }

  @Nonnull
  public <R> Result<R> convert(@Nullable Function<T, R> function) {
    Result<R> retRes = new Result<>();
    retRes.setTraceId(this.getTraceId());
    retRes.setSuccess(this.isSuccess());
    retRes.setCode(this.getCode());
    retRes.setMessage(this.getMessage());
    if (this.getData() != null && function != null) {
      retRes.setData(function.apply(this.getData()));
    }
    return retRes;
  }

  /**
   * 如果断言响应结果不可能为空可以调用此方法获取响应数据, 如果为空则会抛出{@link NullPointerException}
   *
   * @return 响应数据
   * @author 宋志宗 on 2021/4/14
   */
  @Nonnull
  @Transient
  public T requiredData() {
    T data = getData();
    throwWhenFailure();
    if (data == null) {
      throw new NullPointerException("data is null");
    }
    return data;
  }
}
