package com.korol.network.api.home

import com.korol.network.api.home.model.HotSalesAndBestSellerResponse
import retrofit2.Call
import retrofit2.http.GET

interface HomeRetrofitServices {
    @GET("/v3/654bd15e-b121-49ba-a588-960956b15175")
    fun getHotSalesAndBestSeller(
    ): Call<HotSalesAndBestSellerResponse>
}