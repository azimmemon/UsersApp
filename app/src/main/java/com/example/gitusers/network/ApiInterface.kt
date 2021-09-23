package com.example.gitusers.network

import com.example.gitusers.domain.response.UsersListResponse
import com.example.gitusers.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET(Constants.USERS_SEARCH)
    suspend fun getUsersList(
        @Query("q") searchQuery: String,
        @Query("page") pageNo: Int,

    ): UsersListResponse

}