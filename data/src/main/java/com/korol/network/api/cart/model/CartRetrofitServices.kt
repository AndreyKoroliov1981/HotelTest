package com.korol.network.api.cart.model

import retrofit2.Call
import retrofit2.http.GET

interface CartRetrofitServices {
    @GET("/v3/53539a72-3c5f-4f30-bbb1-6ca10d42c149")
    fun getCart(): Call<BasketResponse>
}