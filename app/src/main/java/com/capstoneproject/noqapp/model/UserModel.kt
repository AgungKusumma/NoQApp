package com.capstoneproject.noqapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("password")
    val password: String,

    @field:SerializedName("isLogin")
    val isLogin: Boolean,

    @field:SerializedName("token")
    val token: String,

    @field:SerializedName("isAdmin")
    val isAdmin: Boolean,
) : Parcelable
