package com.korol.repository.cart

import com.korol.network.api.cart.model.Basket
import com.korol.network.api.cart.model.CartRetrofitServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CartRepositoryImpl(
    private val cartMapper: CartMapper,
    private val cartRetrofitServices: CartRetrofitServices
) : com.korol.domain.cart.CartRepository {
    override suspend fun getCart(): Basket =
        withContext(Dispatchers.IO) {
        val response = cartRetrofitServices.getCart().execute().body()
        val data = cartMapper.mapCartFromNetwork(response!!)
        return@withContext data
    }
}