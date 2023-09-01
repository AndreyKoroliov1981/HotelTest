package com.korol.network.api.detail

import com.korol.network.api.detail.model.ProductDetailsResponse
import retrofit2.Call
import retrofit2.http.GET

interface DetailsRetrofitServices {
    @GET("/v3/6c14c560-15c6-4248-b9d2-b4508df7d4f5")
    fun getDetails(
    ): Call<ProductDetailsResponse>
}