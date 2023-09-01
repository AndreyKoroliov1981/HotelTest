package com.korol.network.api.detail

import com.korol.network.BuildConfig

object DetailsCommon {
    val detailsRetrofitService: DetailsRetrofitServices
        get() = DetailsRetrofitClient.getClient(BuildConfig.API_ENDPOINT)
            .create(DetailsRetrofitServices::class.java)
}