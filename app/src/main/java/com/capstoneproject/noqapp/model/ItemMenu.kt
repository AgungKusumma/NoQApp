package com.capstoneproject.noqapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ItemMenu(
    var photo: String,
    var name: String,
    var price: String,
    var totalInCart: Int = 0,
) : Parcelable
