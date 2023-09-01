package com.korol.myapplication.ui.cart.recycler

import com.korol.network.api.cart.model.Gadget

interface RVOnClickBasketListener {
    fun onClicked(item: Gadget)
}