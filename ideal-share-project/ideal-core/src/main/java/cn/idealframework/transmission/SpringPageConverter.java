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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author 宋志宗 on 2021/4/20
 */
public final class SpringPageConverter {
  private SpringPageConverter() {
  }

  @Nonnull
  public static <E> PageInfo<E> page(@Nonnull Page<?> page, @Nonnull List<E> content) {
    long totalElements = page.getTotalElements();
    int totalPages = page.getTotalPages();
    int number = page.getNumber();
    int size = page.getSize();
    PageInfo<E> pageImpl = new PageInfo<>();
    pageImpl.setPageNumber(number + 1);
    pageImpl.setPageSize(size);
    pageImpl.setTotalElements(Math.toIntExact(totalElements));
    pageImpl.setTotalPages(totalPages);
    pageImpl.setContent(content);
    return pageImpl;
  }

  @Nonnull
  public static <E> PageInfo<E> page(@Nonnull Page<E> page) {
    return page(page, page.getContent());
  }

  @Nonnull
  public static <E, U> PageInfo<U> page(@Nonnull Page<E> page,
                                        @Nonnull Function<? super E, ? extends U> converter) {
    List<U> content = page.getContent().stream().map(converter).collect(Collectors.toList());
    return page(page, content);
  }

  @Nonnull
  public static Pageable pageable(@Nonnull SortablePaging paging) {
    int page = paging.getPageNumber();
    int size = paging.getPageSize();
    List<Sort> pageSorts = paging.getPageSorts();

    List<org.springframework.data.domain.Sort.Order> orderList = null;
    if (pageSorts != null && pageSorts.size() > 0) {
      orderList = new ArrayList<>();
      for (Sort sort : pageSorts) {
        Sort.Direction direction = sort.getDirection();
        if (direction.isAscending()) {
          orderList.add(org.springframework.data.domain.Sort.Order.asc(sort.getProperty()));
        } else {
          orderList.add(org.springframework.data.domain.Sort.Order.desc(sort.getProperty()));
        }
      }
    }
    // 我们的页码从1开始, spring的页码从0开始, 转换一下
    if (orderList != null) {
      return PageRequest.of(page - 1, size, org.springframework.data.domain.Sort.by(orderList));
    } else {
      return PageRequest.of(page - 1, size);
    }
  }

  @Nonnull
  public static Pageable pageable(@Nonnull Paging paging) {
    if (paging instanceof SortablePaging) {
      return pageable((SortablePaging) paging);
    }
    int page = paging.getPageNumber();
    int size = paging.getPageSize();
    return PageRequest.of(page - 1, size);
  }

  @Nonnull
  @SuppressWarnings("DuplicatedCode")
  public static <E> PageResult<E> pageResult(@Nonnull Page<E> page) {
    PageResult<E> res = new PageResult<>();
    res.setPage(page.getNumber() + 1);
    res.setSize(page.getSize());
    res.setTotal(page.getTotalElements());
    res.setTotalPages(page.getTotalPages());
    res.setData(page.getContent());
    res.setSuccess(true);
    res.setCode(ResMsg.SUCCESS.code());
    res.setMessage(ResMsg.SUCCESS.message());
    return res;
  }
}
