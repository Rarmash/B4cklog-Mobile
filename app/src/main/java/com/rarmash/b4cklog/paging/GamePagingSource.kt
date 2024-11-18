package com.rarmash.b4cklog.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rarmash.b4cklog.models.Game
import com.rarmash.b4cklog.network.ApiService

class GamePagingSource(private val apiService: ApiService) : PagingSource<Int, Game>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Game> {
        val page = params.key ?: 1
        return try {
            val response = apiService.getGames(page, params.loadSize)

            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                val games = body.results

                LoadResult.Page(
                    data = games,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = body.next?.let { page + 1 }
                )
            } else {
                LoadResult.Error(Exception("Ошибка загрузки данных"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Game>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
