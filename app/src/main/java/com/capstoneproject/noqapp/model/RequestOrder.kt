package com.capstoneproject.noqapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RequestOrder(

	@field:SerializedName("tableId")
	val tableId: String,

	@field:SerializedName("orderItems")
	val orderItems: List<OrderItems>,
) : Parcelable

@Parcelize
data class OrderItems(

	@field:SerializedName("menuId")
	val menuId: String,

	@field:SerializedName("amount")
	val amount: Int,
) : Parcelable
