package com.dicoding.gencara.data.user_config

import com.dicoding.gencara.data.response.PredictionResponse
import com.dicoding.gencara.data.response.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UserService {
    @Multipart
    @POST("users")
    fun postUser(
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part photo: MultipartBody.Part?
    ): Call<UserResponse>
}