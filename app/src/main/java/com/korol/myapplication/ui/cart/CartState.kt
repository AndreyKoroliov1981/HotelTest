package com.korol.myapplication.ui.cart

import com.korol.network.api.cart.model.Basket

data class CartState(
    val basket: Basket? = null
)