package com.korol.network.api.detail

object DetailsCommon {
    val detailsRetrofitService: DetailsRetrofitServices
        get() = DetailsRetrofitClient.getClient("https://run.mocky.io")
            .create(DetailsRetrofitServices::class.java)
}
