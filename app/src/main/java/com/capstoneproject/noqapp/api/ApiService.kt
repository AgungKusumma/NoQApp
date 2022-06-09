package com.capstoneproject.noqapp.api

import retrofit2.Call
import retrofit2.http.*

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

    @FormUrlEncoded
    @PATCH("orders/{orderId}")
    fun updateStatus(
        @Path("orderId") orderId: String,
        @Field("status") status: String,
    ): Call<FileUploadResponseAdmin>
}