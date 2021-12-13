package com.reserach.githubapp.data.model

import com.google.gson.annotations.SerializedName

data class UserResponse(
    val items: ArrayList<SearchResponse>
)
