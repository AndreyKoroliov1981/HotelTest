package com.korol.domain.cart

import com.korol.network.api.cart.model.Basket

interface CartInteractor {
    suspend fun getCart(): Basket
}