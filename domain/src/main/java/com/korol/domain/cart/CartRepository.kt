package com.korol.domain.cart

import com.korol.network.api.cart.model.Basket

interface CartRepository {
    suspend fun getCart(): Basket
}