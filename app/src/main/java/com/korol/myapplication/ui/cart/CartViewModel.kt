package com.korol.myapplication.ui.cart

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.korol.domain.cart.CartInteractor
import com.korol.myapplication.common.BaseViewModel
import com.korol.myapplication.common.IsNotCartData
import com.korol.network.api.cart.model.Gadget
import kotlinx.coroutines.launch

class CartViewModel(private val cartInteractor: CartInteractor) :
    BaseViewModel<CartState>(CartState()) {

    init {
        updateData()
    }

    private fun updateData() {
        viewModelScope.launch {
            try {
                val data = cartInteractor.getCart()
                updateState {
                    copy(
                        basket = data
                    )
                }
            } catch (e: Exception) {
                Log.d("my_tag", "cart data error = ${e.message}")
                sideEffectSharedFlow.emit(IsNotCartData(errorMessage = e.message.toString()))
            }
        }
    }

    fun onRetryClick() {
        updateData()
    }

    fun onClickedBasket(item: Gadget) {
        val newBasket = mutableListOf<Gadget>()
        val oldBasket = stateFlow.value.basket!!.basket
        var totalPrice = 0
        for (i in oldBasket.indices) {
            if (item.id != oldBasket[i].id) {
                newBasket.add(oldBasket[i])
                totalPrice += oldBasket[i].price * oldBasket[i].count
            }
        }
        val newCart = stateFlow.value.basket!!.copy(basket = newBasket, total = totalPrice)
        updateState { copy(basket = newCart) }
    }

    fun onClickedCount(item: Gadget, delta: Int) {
        val newBasket = mutableListOf<Gadget>()
        val oldBasket = stateFlow.value.basket!!.basket
        var totalPrice = 0
        for (i in oldBasket.indices) {
            if (item.id != oldBasket[i].id) {
                newBasket.add(oldBasket[i])
                totalPrice += oldBasket[i].price * oldBasket[i].count
            } else {
                if ((oldBasket[i].count == 1) && (delta == -1)) {}
                else{
                    val newCount = oldBasket[i].count + delta
                    val newGadget = oldBasket[i].copy(count = newCount)
                    newBasket.add(newGadget)
                    totalPrice += newGadget.price * newGadget.count
                }
            }
        }
        val newCart = stateFlow.value.basket!!.copy(basket = newBasket, total = totalPrice)
        updateState { copy(basket = newCart) }
    }

}