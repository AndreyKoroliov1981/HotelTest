package com.korol.domain.home

import com.korol.network.api.home.model.HotSalesAndBestSeller

interface HomeInteractor {
    suspend fun getHotSalesAndBestSeller(): HotSalesAndBestSeller
}