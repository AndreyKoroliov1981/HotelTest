package com.korol.network.api.cart.model

data class Basket (
    val basket: List<Gadget>,
    val delivery: String,
    val id: String,
    val total: Int
)

data class Gadget(
    val id: Int,
    val images: String,
    val price: Int,
    val title: String,
    val count: Int = 1
)