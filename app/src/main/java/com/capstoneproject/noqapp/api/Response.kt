package com.capstoneproject.noqapp.api

import android.os.Parcelable
import com.capstoneproject.noqapp.admin.model.DetailOrderModel
import com.capstoneproject.noqapp.admin.model.Order
import com.capstoneproject.noqapp.model.MenuModel
import com.capstoneproject.noqapp.model.UserModel
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class FileUploadResponse(
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("loginResult")
    val loginResult: UserModel,

    @field:SerializedName("data")
    val data: ArrayList<MenuModel>,
) : Parcelable

@Parcelize
data class FileUploadResponseAdmin(
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

    @field:SerializedName("data")
    val data: ArrayList<Order>,

    @field:SerializedName("orderItems")
    val orderItems: ArrayList<DetailOrderModel>,
) : Parcelable
