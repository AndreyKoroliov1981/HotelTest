package com.korol.repository.home

import com.korol.network.api.home.HomeRetrofitServices
import com.korol.network.api.home.model.HotSalesAndBestSeller
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HomeRepositoryImpl(
    private val homeMapper:  HomeMapper,
    private var homeRetrofitServices: HomeRetrofitServices
    ) : com.korol.domain.home.HomeRepository {

    override suspend fun getHotSalesAndBestSeller(): HotSalesAndBestSeller =
        withContext(Dispatchers.IO) {
            val response = homeRetrofitServices.getHotSalesAndBestSeller().execute().body()
            val dataList = homeMapper.mapHotSalesAndBestSellerFromNetwork(response!!)
            return@withContext dataList
        }
}