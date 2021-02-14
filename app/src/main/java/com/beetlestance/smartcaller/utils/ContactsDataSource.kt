package com.beetlestance.smartcaller.utils

import androidx.paging.PagingSource
import com.beetlestance.smartcaller.di.AppCoroutineDispatchers
import kotlinx.coroutines.withContext

class ContactsDataSource<T : Any>(
    private val dispatcher: AppCoroutineDispatchers,
    private val map: suspend (Int, Int?) -> List<T>
) : PagingSource<Int, T>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val data = map(params.loadSize, params.key)
        return withContext(dispatcher.io) {
            LoadResult.Page(
                data = data,
                prevKey = guessPrevKey(params, data),
                nextKey = guessNextKey(params, data)
            )
        }
    }

    private fun guessPrevKey(params: LoadParams<Int>, data: List<T>): Int? {
        return when (params) {
            is LoadParams.Refresh -> null
            is LoadParams.Prepend -> if (data.isEmpty()) null else params.key - data.size
            is LoadParams.Append -> params.key
        }
    }

    private fun guessNextKey(params: LoadParams<Int>, data: List<T>): Int? {
        return when (params) {
            is LoadParams.Refresh -> params.loadSize
            is LoadParams.Prepend -> params.key
            is LoadParams.Append -> if (data.isEmpty()) null else params.key + data.size
        }
    }

}