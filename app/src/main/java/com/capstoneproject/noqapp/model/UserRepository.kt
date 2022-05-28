package com.capstoneproject.noqapp.model

import com.capstoneproject.noqapp.api.ApiService
import com.capstoneproject.noqapp.api.FileUploadResponse
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