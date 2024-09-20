package com.example.github.presentation.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User(
    @SerializedName("login")
    val login: String,

    @SerializedName("id")
    val id: Int,

    @SerializedName("avatar_url")
    val avatarUrl: String,

    @SerializedName("followers")
    val followers: String,

    @SerializedName("following")
    val following: String,

    @SerializedName("bio")
    val bio: String,

    @SerializedName("location")
    val location: String
) : Serializable