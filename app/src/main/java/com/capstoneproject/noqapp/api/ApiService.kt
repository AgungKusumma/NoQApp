package com.capstoneproject.noqapp.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("auth/register")
    fun userRegister(
        @Body user: Map<String, String>,
    ): Call<FileUploadResponse>

    @POST("auth/login")
    fun userLogin(
        @Body user: Map<String, String>,
    ): Call<FileUploadResponse>
}