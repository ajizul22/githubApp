package com.reserach.githubapp.api

import com.reserach.githubapp.data.model.User
import com.reserach.githubapp.data.model.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getUsersBySearch(
        @Query("q") query: String,
        @Query("per_page") items: Int,
        @Query("page") page: Int
    ): Call<UserResponse>

    @GET("users")
    @Headers("Authorization: token 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")
    fun getUsers(
        @Query("per_page") items: Int
    ): Call<ArrayList<User>>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<User>
}