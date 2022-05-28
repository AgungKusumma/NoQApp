package com.capstoneproject.noqapp.api

import android.os.Parcelable
import com.capstoneproject.noqapp.model.UserModel
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class FileUploadResponse(
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("loginResult")
    val loginResult: UserModel,
) : Parcelable
