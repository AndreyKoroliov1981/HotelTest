package com.korol.network.api.cart.model

data class BasketResponse(
    val basket: List<GadgetResponse>,
    val delivery: String,
    val id: String,
    val total: Int
)

data class GadgetResponse(
    val id: Int,
    val images: String,
    val price: Int,
    val title: String
)