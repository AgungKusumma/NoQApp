package com.capstoneproject.noqapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MenuModel(
	@field:SerializedName("menuId")
	val menuId: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("price")
	val price: Int,

	@field:SerializedName("photoUrl")
	val photoUrl: String,

	var totalInCart: Int = 0,
) : Parcelable
