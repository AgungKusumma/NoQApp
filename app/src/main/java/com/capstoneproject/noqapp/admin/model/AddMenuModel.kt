package com.capstoneproject.noqapp.admin.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddMenuModel(

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("price")
    val price: Int,

    @field:SerializedName("photoUrl")
    val photoUrl: String,
) : Parcelable
