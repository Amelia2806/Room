package com.example.authorlisst.network

import com.example.authorlisst.model.ApiData
import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("results")
    val results: List<ApiData>
)
