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

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.annotation.Nonnull;
import java.beans.Transient;

/**
 * @author 宋志宗 on 2021/4/29
 */
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Paging {
  private int pageNumber = 1;
  private int pageSize = 10;

  public int getPageNumber() {
    return pageNumber;
  }

  public int getPageSize() {
    return pageSize;
  }

  @Nonnull
  public static Paging of(int pageNumber, int pageSize) {
    return new Paging(pageNumber, pageSize);
  }

  @Nonnull
  public SortablePaging ascBy(@Nonnull String property) {
    SortablePaging paging = new SortablePaging();
    paging.setPageNumber(this.getPageNumber());
    paging.setPageSize(this.getPageSize());
    return paging.ascBy(property);
  }

  @Transient
  public long getOffset() {
    return ((long) pageNumber - 1L) * (long) pageSize;
  }

  @Nonnull
  public SortablePaging descBy(@Nonnull String property) {
    SortablePaging paging = new SortablePaging();
    paging.setPageNumber(this.getPageNumber());
    paging.setPageSize(this.getPageSize());
    return paging.descBy(property);
  }
}
