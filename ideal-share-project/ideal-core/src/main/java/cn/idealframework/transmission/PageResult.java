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

import cn.idealframework.lang.Lists;
import cn.idealframework.transmission.exception.ResultException;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.annotation.Nonnull;
import java.beans.Transient;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author 宋志宗 on 2021/8/25
 */
@Getter
@Setter
@Accessors(chain = true)
public class PageResult<E> extends BasicResult {
  private static final long serialVersionUID = -7093873606301836395L;

  /** 页码 */
  private int page;

  /** 页大小 */
  private int size;

  /** 总数量 */
  private long total;

  /** 总页数 */
  private int totalPages;

  /** 分页内容 */
  @Nonnull
  private List<E> data = Collections.emptyList();

  public PageResult() {
    super();
  }

  @Nonnull
  public static <E> PageResult<E> empty(@Nonnull Paging paging) {
    PageResult<E> res = new PageResult<>();
    res.setPage(paging.getPageNumber());
    res.setSize(paging.getPageSize());
    res.setTotal(0);
    res.setTotalPages(1);
    res.setData(new ArrayList<>());
    res.setSuccess(true);
    res.setCode(ResMsg.SUCCESS.code());
    res.setMessage(ResMsg.SUCCESS.message());
    return res;
  }

  @Nonnull
  public static <E> PageResult<E> singleton(@Nonnull Paging paging, @Nonnull E element) {
    PageResult<E> res = new PageResult<>();
    res.setPage(paging.getPageNumber());
    res.setSize(paging.getPageSize());
    res.setTotal(1);
    res.setTotalPages(1);
    res.setData(Lists.arrayList(element));
    res.setSuccess(true);
    res.setCode(ResMsg.SUCCESS.code());
    res.setMessage(ResMsg.SUCCESS.message());
    return res;
  }

  @Nonnull
  @SuppressWarnings("DuplicatedCode")
  public static <E> PageResult<E> of(@Nonnull PageInfo<E> pageInfo) {
    PageResult<E> res = new PageResult<>();
    res.setPage(pageInfo.getPageNumber());
    res.setSize(pageInfo.getPageSize());
    res.setTotal(pageInfo.getTotalElements());
    res.setTotalPages(pageInfo.getTotalPages());
    res.setData(pageInfo.getContent());
    res.setSuccess(true);
    res.setCode(ResMsg.SUCCESS.code());
    res.setMessage(ResMsg.SUCCESS.message());
    return res;
  }

  @Nonnull
  public <U> PageResult<U> map(Function<? super E, ? extends U> converter) {
    PageResult<U> res = new PageResult<>();
    res.setSuccess(true);
    res.setCode(this.getCode());
    res.setMessage(this.getMessage());
    res.setPage(this.getPage());
    res.setSize(this.getSize());
    res.setTotal(this.getTotal());
    res.setTotalPages(this.getTotalPages());
    List<E> data = this.getData();
    if (data.size() > 0) {
      res.setData(data.stream().map(converter).collect(Collectors.toList()));
    }
    return res;
  }

  @Nonnull
  public PageInfo<E> toPage() {
    PageInfo<E> pageInfo = new PageInfo<>();
    pageInfo.setPageNumber(getPage());
    pageInfo.setPageSize(getSize());
    pageInfo.setTotalElements(getTotal());
    pageInfo.setTotalPages(getTotalPages());
    pageInfo.setContent(getData());
    return pageInfo;
  }

  @Nonnull
  public PageInfo<E> toPageOrThrow() throws ResultException {
    onFailureThrow();
    return toPage();
  }

  @Nonnull
  @Transient
  public List<E> getOrThrow() throws ResultException {
    onFailureThrow();
    return getData();
  }
}
