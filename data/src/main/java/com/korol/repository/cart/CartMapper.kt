package com.korol.repository.cart

import com.korol.network.api.cart.model.Basket
import com.korol.network.api.cart.model.BasketResponse
import com.korol.network.api.cart.model.Gadget
import com.korol.network.api.cart.model.GadgetResponse

class CartMapper {
    fun mapCartFromNetwork(basketResponse: BasketResponse): Basket =
        Basket(
            basket = mapGadgetResponseFromNetwork(basketResponse.basket),
            delivery = basketResponse.delivery,
            id = basketResponse.id,
            total = basketResponse.total
        )

    private fun mapGadgetResponseFromNetwork(gadgetResponseFromNetwork: List<GadgetResponse>): List<Gadget> {
        val list = mutableListOf<Gadget>()
        for (i in gadgetResponseFromNetwork.indices) {
            val gadget = Gadget(
                id = gadgetResponseFromNetwork[i].id,
                images = gadgetResponseFromNetwork[i].images,
                price = gadgetResponseFromNetwork[i].price,
                title = gadgetResponseFromNetwork[i].title
                )
            list.add(gadget)
        }
        return list
    }
}