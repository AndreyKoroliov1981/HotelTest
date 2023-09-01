package com.korol.network.api.home.model

import com.google.gson.annotations.SerializedName

data class HotSalesAndBestSellerResponse(
    @SerializedName("home_store") val homeStore: List<HomeStoreResponse>,
    @SerializedName("best_seller") val bestSeller: List<BestSellerResponse>
)

data class HomeStoreResponse(
    val id: Int,
    @SerializedName("is_new") val isNew: Boolean,
    val title: String,
    val subtitle: String,
    val picture: String,
    @SerializedName("is_buy") val isBuy: Boolean,
)

data class BestSellerResponse(
    val id: Int,
    @SerializedName("is_favorites") val isFavorites: Boolean,
    val title: String,
    @SerializedName("price_without_discount") val priceWithoutDiscount: Int,
    @SerializedName("discount_price") val discountPrice: Int,
    val picture: String
)

