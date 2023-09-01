package com.korol.domain.cart

import com.korol.network.api.cart.model.Basket

class CartInteractorImpl(
    private val cartRepository: CartRepository
) : CartInteractor {
    override suspend fun getCart(): Basket {
        return cartRepository.getCart()
    }
}