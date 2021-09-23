package com.example.gitusers.domain.usecase

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.gitusers.domain.response.Items
import com.example.gitusers.repository.UsersRepository

class FetchUsersUseCase(var mUsersRepository: UsersRepository): PagingSource<Int, Items>() {

    private var searchQuery = ""

    fun setParams(searchQuery: String) {
        this.searchQuery = searchQuery
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Items> {

        return try {
            val nextPageNumber = params.key ?: 0
            val response = mUsersRepository.getUsersList(searchQuery, nextPageNumber)
            var sortedData = response.items.sortedWith( compareByDescending { item -> item.name })

            LoadResult.Page(
                data = sortedData,
                prevKey = if (nextPageNumber > 0) nextPageNumber - 1 else null,
                nextKey = if (nextPageNumber < 8) nextPageNumber + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)

        }
    }

    override fun getRefreshKey(state: PagingState<Int, Items>): Int? {
        TODO("Not yet implemented")
    }

}

