package com.example.authorlisst.network

import com.example.authorlisst.model.ApiData
import retrofit2.Call

import retrofit2.http.GET

interface ApiService {

    @GET("character")
    fun getApiData(): Call<ApiResponse>
}