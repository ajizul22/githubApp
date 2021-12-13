package com.reserach.githubapp.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("login")
    val userName: String,
    val id: Int,
    val avatar_url: String,
    val location: String,
    val email: String,
    @SerializedName("created_at")
    val createdAt: String
)
