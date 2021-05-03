package com.beetlestance.smartcaller.utils

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.beetlestance.smartcaller.di.AppCoroutineDispatchers
import kotlinx.coroutines.withContext

class CursorPagingSource<T : Any>(
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
            is LoadParams.Refresh -> params.key
            is LoadParams.Prepend -> {
                if (data.isEmpty()) null else (params.key - data.size)
            }
            is LoadParams.Append -> params.key
        }
    }

    private fun guessNextKey(params: LoadParams<Int>, data: List<T>): Int? {
        return when (params) {
            is LoadParams.Refresh -> {
                if (params.key == null) params.loadSize else (params.key ?: 0) + data.size
            }
            is LoadParams.Prepend -> params.key
            is LoadParams.Append -> if (data.isEmpty()) null else params.key + data.size
        }
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            // This loads starting from previous page, but since PagingConfig.initialLoadSize spans
            // multiple pages, the initial load will still load items centered around
            // anchorPosition. This also prevents needing to immediately launch prepend due to
            // prefetchDistance.
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }
}