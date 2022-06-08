package com.capstoneproject.noqapp.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("auth/register")
    fun userRegister(
        @Body user: Map<String, String>,
    ): Call<FileUploadResponse>

    @POST("auth/login")
    fun userLogin(
        @Body user: Map<String, String>,
    ): Call<FileUploadResponse>

    @GET("menus")
    fun getMenu(): Call<FileUploadResponse>

    @GET("orders")
    fun getOrder(): Call<FileUploadResponseAdmin>

    @GET("orders/{orderId}")
    fun getDetailOrder(
        @Path("orderId") orderId: String,
    ): Call<FileUploadResponseAdmin>
}