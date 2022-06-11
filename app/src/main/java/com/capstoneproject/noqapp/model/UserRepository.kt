package com.capstoneproject.noqapp.model

import com.capstoneproject.noqapp.admin.model.AddMenuModel
import com.capstoneproject.noqapp.api.ApiConfig
import com.capstoneproject.noqapp.api.ApiService
import com.capstoneproject.noqapp.api.FileUploadResponse
import com.capstoneproject.noqapp.api.FileUploadResponseAdmin
import com.capstoneproject.noqapp.utils.AppExecutors
import retrofit2.Call

class UserRepository private constructor(
    private val apiService: ApiService,
    val appExecutors: AppExecutors,
) {
    fun userLogin(email: String, password: String): Call<FileUploadResponse> {
        val user: Map<String, String> = mapOf(
            "email" to email,
            "password" to password
        )

        return apiService.userLogin(user)
    }

    fun userRegister(name: String, email: String, password: String): Call<FileUploadResponse> {
        val user: Map<String, String> = mapOf(
            "name" to name,
            "email" to email,
            "password" to password
        )

        return apiService.userRegister(user)
    }

    fun getMenu(token: String): Call<FileUploadResponse> {
        return ApiConfig().getApiWithToken(token).getMenu()
    }

    fun getRecommend(token: String): Call<FileUploadResponse> {
        return ApiConfig().getApiWithToken(token).getRecommend()
    }

    fun getOrderHistory(token: String): Call<FileUploadResponseAdmin> {
        return ApiConfig().getApiWithToken(token).getOrderHistory()
    }

    fun getDetailOrderHistory(orderId: String, token: String): Call<FileUploadResponseAdmin> {
        return ApiConfig().getApiWithToken(token).getDetailOrderHistory(orderId)
    }

    fun orderItem(
        requestOrder: RequestOrder,
        token: String,
    ): Call<FileUploadResponseAdmin> {
        return ApiConfig().getApiWithToken(token).orderItem(requestOrder)
    }

    fun getOrder(token: String): Call<FileUploadResponseAdmin> {
        return ApiConfig().getApiWithToken(token).getOrder()
    }

    fun getDetailOrder(orderId: String, token: String): Call<FileUploadResponseAdmin> {
        return ApiConfig().getApiWithToken(token).getDetailOrder(orderId)
    }

    fun updateStatus(orderId: String, status: String, token: String)
            : Call<FileUploadResponseAdmin> {
        return ApiConfig().getApiWithToken(token).updateStatus(orderId, status)
    }

    fun addMenu(
        menu: AddMenuModel,
        token: String,
    ): Call<FileUploadResponseAdmin> {
        return ApiConfig().getApiWithToken(token).addMenu(menu)
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            appExecutors: AppExecutors,
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, appExecutors)
            }.also { instance = it }
    }
}