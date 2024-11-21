package com.rarmash.b4cklog.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rarmash.b4cklog.models.Game
import com.rarmash.b4cklog.network.ApiService

class SearchPagingSource(
    private val apiService: ApiService,
    private val query: String
) : PagingSource<Int, Game>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Game> {
        val page = params.key ?: 1
        return try {
            val response = apiService.searchGames(query, page, params.loadSize)
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                val games = body.results

                LoadResult.Page(
                    data = games,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = body.next?.let { page + 1 }
                )
            } else {
                LoadResult.Error(Exception("Error loading data"))
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