package com.example.gitusers.domain.response

import com.squareup.moshi.Json

data class UsersListResponse(
    @field:Json(name = "items")
    var items: List<Items>

)
data class Items(
    @field:Json(name = "id")
    var id: String?,
    @field:Json(name = "login")
    var name: String?,
    @field:Json(name = "avatar_url")
    var avatar_url: String?,
    @field:Json(name = "type")
    var type: String?

)
