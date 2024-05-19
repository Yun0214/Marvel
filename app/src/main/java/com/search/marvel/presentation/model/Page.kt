package com.search.marvel.presentation.model

import java.io.Serializable

data class Page<T>(
    val keyword: String, val hasNextPage: Boolean = false, val pageIndex: Int = 0, private val data: List<T> = emptyList()
) : Serializable {
    fun getList() = data
}

fun <R> Page<*>.switchData(newData: List<R>): Page<R> {
    return Page(keyword, hasNextPage, pageIndex, newData)
}

suspend fun <T, R> Page<T>.map(convert: suspend (item: T) -> R?): List<R> {
    return getList().mapNotNull { convert(it) }
}
