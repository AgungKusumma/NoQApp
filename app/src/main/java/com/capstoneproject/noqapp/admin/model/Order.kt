package com.capstoneproject.noqapp.admin.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order(
    @field:SerializedName("orderId")
    val orderId: String,

    @field:SerializedName("userId")
    val userId: String,

    @field:SerializedName("tableId")
    val tableId: String,

    @field:SerializedName("totalPrice")
    val totalPrice: Int,

    @field:SerializedName("status")
    val status: String,
) : Parcelable