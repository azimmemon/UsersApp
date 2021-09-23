package com.example.gitusers.repository

import com.example.gitusers.domain.response.UsersListResponse
import com.example.gitusers.network.ApiInterface

class UsersRepository(private var mApiService: ApiInterface) {

    suspend fun getUsersList(searchQuery: String, pageNumber: Int): UsersListResponse {
        return mApiService.getUsersList(searchQuery, pageNumber)
    }
}