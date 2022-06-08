package com.capstoneproject.noqapp.model

import com.capstoneproject.noqapp.api.ApiService
import com.capstoneproject.noqapp.api.FileUploadResponse
import com.capstoneproject.noqapp.api.FileUploadResponseAdmin
import com.capstoneproject.noqapp.utils.ApiInterceptor
import com.capstoneproject.noqapp.utils.AppExecutors
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
        val client = OkHttpClient.Builder()
            .addInterceptor(ApiInterceptor(token))
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("http://34.101.136.198:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        val mApiService = retrofit.create(ApiService::class.java)
        return mApiService.getMenu()
    }

    fun getOrder(token: String): Call<FileUploadResponseAdmin> {
        val client = OkHttpClient.Builder()
            .addInterceptor(ApiInterceptor(token))
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("http://34.101.136.198:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        val mApiService = retrofit.create(ApiService::class.java)
        return mApiService.getOrder()
    }

    fun getDetailOrder(orderId: String, token: String): Call<FileUploadResponseAdmin> {
        val client = OkHttpClient.Builder()
            .addInterceptor(ApiInterceptor(token))
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("http://34.101.136.198:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        val mApiService = retrofit.create(ApiService::class.java)
        return mApiService.getDetailOrder(orderId)
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