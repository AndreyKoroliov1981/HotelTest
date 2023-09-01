package com.korol.network.api.home

import com.korol.network.BuildConfig

object HomeCommon {
    val homeRetrofitService: HomeRetrofitServices
        get() = HomeRetrofitClient.getClient(BuildConfig.API_ENDPOINT)
            .create(HomeRetrofitServices::class.java)
}