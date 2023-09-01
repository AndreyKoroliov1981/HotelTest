package com.korol.network.api.home.model

data class HotSalesAndBestSeller(
    val homeStore: List<HomeStore>,
    val bestSeller: List<BestSeller>
)

data class HomeStore(
    val id: Int,
    val isNew: Boolean,
    val title: String,
    val subtitle: String,
    val picture: String,
    val isBuy: Boolean,
)

data class BestSeller(
    val id: Int,
    val isFavorites: Boolean,
    val title: String,
    val priceWithoutDiscount: Int,
    val discountPrice: Int,
    val picture: String
)

