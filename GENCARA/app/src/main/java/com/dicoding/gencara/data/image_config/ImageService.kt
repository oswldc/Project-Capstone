package com.dicoding.gencara.data.image_config

import com.dicoding.gencara.data.response.PredictionResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ImageService {
    @Multipart
    @POST("predict")
    fun postPredict(@Part file: MultipartBody.Part): Call<PredictionResponse>
}
