package com.korol.myapplication.ui.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.korol.domain.cart.CartInteractor
import com.korol.domain.details.DetailsInteractor
import com.korol.myapplication.common.BaseViewModel
import com.korol.myapplication.common.IsNotDetailsData
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class DetailsViewModel @AssistedInject constructor(
    private val detailsInteractor: DetailsInteractor,
    private val cartInteractor: CartInteractor,
    @Assisted private val productId: Int
) : BaseViewModel<DetailsState>(DetailsState()) {

    @AssistedFactory
    interface DetailsViewModelFactory {
        fun create(productId: Int): DetailsViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun providesFactory(
            assistedFactory: DetailsViewModelFactory,
            productId: Int
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(productId) as T
            }
        }
    }

    private fun updateData() {
        viewModelScope.launch {
            try {
                val data = detailsInteractor.getDetails(productId)
                val data2 = cartInteractor.getCart()
                updateState {
                    copy(
                        detailsInfo = data,
                        countProductsInCart = data2.basket.size
                    )
                }
                updateState {
                    copy(
                        initPager = true
                    )
                }
            } catch (e: Exception) {
                Log.d("my_tag", "details info data error = ${e.message}")
                sideEffectSharedFlow.emit(IsNotDetailsData(errorMessage = e.message.toString()))
            }
        }
    }

    fun onSwipe(delta: Int) {
        if (stateFlow.value.detailsInfo != null) {
            if (delta > 0) {
                if (stateFlow.value.currentImage + 1 < stateFlow.value.detailsInfo!!.images.size) {
                    updateState { copy(currentImage = currentImage + 1) }
                }
            } else {
                if (stateFlow.value.currentImage - 1 >= 0) {
                    updateState { copy(currentImage = currentImage - 1) }
                }
            }
        }
    }

    fun onColorClick(number: Int) {
        if (number != stateFlow.value.currentColor) {
            updateState { copy(currentColor = number) }
        }
    }

    fun onMemoryClick(number: Int) {
        if (number != stateFlow.value.currentMemorySize) {
            updateState { copy(currentMemorySize = number) }
        }
    }

    fun onRetryClick() {
        updateData()
    }

    fun onCreateUpdate() {
        updateState { copy(detailsInfo = null, initPager = false) }
        updateData()
    }

    fun onButtonAddToCartClick() {
        updateState { copy(countProductsInCart = stateFlow.value.countProductsInCart + 1) }
    }
}