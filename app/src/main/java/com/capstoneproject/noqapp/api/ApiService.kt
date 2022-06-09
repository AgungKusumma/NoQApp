package com.capstoneproject.noqapp.api

import com.capstoneproject.noqapp.model.RequestOrder
import retrofit2.Call
import retrofit2.http.*

import retrofit2.http.FormUrlEncoded

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

    @GET("menus")
    fun getMenu(): Call<FileUploadResponse>

    @POST("orders/new")
    fun orderItem(
        @Body requestBody: RequestOrder
    ): Call<FileUploadResponseAdmin>

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