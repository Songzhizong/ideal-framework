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

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * @author 宋志宗 on 2021/8/25
 */
@Getter
@Setter
@Accessors(chain = true)
public class PageInfo<E> {

  /** 页码 */
  private int pageNumber;

  /** 页大小 */
  private int pageSize;

  /** 总数量 */
  private long totalElements;

  /** 总页数 */
  private int totalPages;

  /** 分页内容 */
  @Nonnull
  private List<E> content = Collections.emptyList();

  @Nonnull
  public static <E> PageInfo<E> empty(@Nonnull Paging paging) {
    PageInfo<E> page = new PageInfo<>();
    page.setPageNumber(paging.getPageNumber());
    page.setPageSize(paging.getPageSize());
    page.setTotalElements(0);
    page.setTotalPages(1);
    page.setContent(Collections.emptyList());
    return page;
  }

  @Nonnull
  public static <E> PageInfo<E> singleton(@Nonnull Paging paging, E element) {
    PageInfo<E> page = new PageInfo<>();
    page.setPageNumber(paging.getPageNumber());
    page.setPageSize(paging.getPageSize());
    page.setTotalElements(1);
    page.setTotalPages(1);
    page.setContent(Collections.singletonList(element));
    return page;
  }

  @Nonnull
  public <U> PageInfo<U> map(@Nonnull Function<? super E, ? extends U> converter) {
    PageInfo<U> page = new PageInfo<>();
    page.setPageNumber(this.getPageNumber());
    page.setPageSize(this.getPageSize());
    page.setTotalElements(this.getTotalElements());
    page.setTotalPages(this.getTotalPages());
    List<E> content = this.getContent();
    List<U> contentList = new ArrayList<>();
    for (E e : content) {
      U apply = converter.apply(e);
      contentList.add(apply);
    }
    page.setContent(contentList);
    return page;
  }
}
