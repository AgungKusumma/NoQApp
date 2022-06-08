package com.capstoneproject.noqapp.admin.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailOrderModel(
    @field:SerializedName("menuId")
    val menuId: String,

    @field:SerializedName("menuPrice")
    val menuPrice: Int,

    @field:SerializedName("amount")
    val amount: Int,

    @field:SerializedName("subtotal")
    val subtotal: Int,
) : Parcelable
