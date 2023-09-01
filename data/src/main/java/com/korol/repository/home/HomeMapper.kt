package com.korol.repository.home

import com.korol.network.api.home.model.*

class HomeMapper {
    fun mapHotSalesAndBestSellerFromNetwork(hotSalesAndBestSellerResponse: HotSalesAndBestSellerResponse) =
        HotSalesAndBestSeller(
            homeStore = mapHomeStoreFromNetwork(hotSalesAndBestSellerResponse.homeStore),
            bestSeller = mapBestSellerFromNetwork(hotSalesAndBestSellerResponse.bestSeller),
        )

    private fun mapHomeStoreFromNetwork(homeStores: List<HomeStoreResponse>): List<HomeStore> {
        val list = mutableListOf<HomeStore>()
        for (i in homeStores.indices) {
            val homeStore = HomeStore(
                id = homeStores[i].id,
                isNew = homeStores[i].isNew,
                title = homeStores[i].title,
                subtitle = homeStores[i].subtitle,
                picture = homeStores[i].picture,
                isBuy = homeStores[i].isBuy,
            )
            list.add(homeStore)
        }
        return list
    }

    private fun mapBestSellerFromNetwork(bestSellers: List<BestSellerResponse>): List<BestSeller> {
        val list = mutableListOf<BestSeller>()
        for (i in bestSellers.indices) {
            val bestSeller = BestSeller(
                id = bestSellers[i].id,
                isFavorites = bestSellers[i].isFavorites,
                title = bestSellers[i].title,
                priceWithoutDiscount = bestSellers[i].priceWithoutDiscount,
                discountPrice = bestSellers[i].discountPrice,
                picture = bestSellers[i].picture,
            )
            list.add(bestSeller)
        }
        return list
    }
}