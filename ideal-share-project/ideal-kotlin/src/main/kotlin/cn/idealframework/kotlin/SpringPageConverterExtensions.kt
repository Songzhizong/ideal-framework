package cn.idealframework.kotlin

import cn.idealframework.transmission.Paging
import cn.idealframework.transmission.SortablePaging
import cn.idealframework.transmission.SpringPageConverter
import org.springframework.data.domain.Page

fun Paging.toPageable() = SpringPageConverter.pageable(this)

fun SortablePaging.toPageable() = SpringPageConverter.pageable(this)

fun <E> Page<E>.toPageResult() = SpringPageConverter.pageResult(this)
