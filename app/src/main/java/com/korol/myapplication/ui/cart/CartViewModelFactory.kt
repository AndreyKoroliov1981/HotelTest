package com.korol.myapplication.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.korol.domain.cart.CartInteractor

class CartViewModelFactory(val cartInteractor: CartInteractor): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CartViewModel(cartInteractor = cartInteractor) as T
    }

}