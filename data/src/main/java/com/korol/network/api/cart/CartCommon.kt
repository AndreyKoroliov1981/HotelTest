package com.korol.network.api.cart

import com.korol.network.BuildConfig
import com.korol.network.api.cart.model.CartRetrofitServices

object CartCommon {
    val cartRetrofitService: CartRetrofitServices
        get() = CartRetrofitClient.getClient(BuildConfig.API_ENDPOINT)
            .create(CartRetrofitServices::class.java)
}