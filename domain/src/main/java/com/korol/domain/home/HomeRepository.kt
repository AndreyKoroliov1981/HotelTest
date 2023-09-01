package com.korol.domain.home

import com.korol.network.api.home.model.HotSalesAndBestSeller

interface HomeRepository {
    suspend fun getHotSalesAndBestSeller(): HotSalesAndBestSeller
}