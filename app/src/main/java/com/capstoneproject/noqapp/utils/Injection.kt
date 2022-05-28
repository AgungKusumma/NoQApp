package com.capstoneproject.noqapp.utils

import android.content.Context
import com.capstoneproject.noqapp.model.UserRepository
import com.capstoneproject.noqapp.api.ApiConfig

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig().getApiService()
        val appExecutors = AppExecutors()
        return UserRepository.getInstance(apiService, appExecutors)
    }
}