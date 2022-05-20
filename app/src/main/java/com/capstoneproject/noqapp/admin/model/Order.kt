package com.capstoneproject.noqapp.admin.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order(
    var timeStamp: String,
    var tableCode: String,
    var username: String,
    var price: String,
    var orderMenu: String
) : Parcelable