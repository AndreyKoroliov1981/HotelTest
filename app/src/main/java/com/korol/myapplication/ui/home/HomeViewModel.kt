package com.korol.myapplication.ui.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.korol.domain.home.HomeInteractor
import com.korol.myapplication.common.BaseViewModel
import com.korol.myapplication.common.IsNotHomeData
import com.korol.network.api.home.model.HomeStore
import kotlinx.coroutines.launch

class HomeViewModel(private val homeInteractor: HomeInteractor) :
    BaseViewModel<HomeState>(HomeState()) {

    init {
        updateData()
    }

    private fun updateData() {
        viewModelScope.launch {
            try {
                val data = homeInteractor.getHotSalesAndBestSeller()
                updateState {
                    copy(
                        hotSalesList = data.homeStore,
                        bestSellerList = data.bestSeller
                    )
                }
            } catch (e: Exception) {
                Log.d("my_tag", "home data error = ${e.message}")
                sideEffectSharedFlow.emit(IsNotHomeData(errorMessage = e.message.toString()))
            }
        }
    }

    fun onFilterClick() {

    }

    fun onSwipe(delta: Int) {
            if (delta > 0) {
                if (stateFlow.value.currentHotSales + 1 < stateFlow.value.hotSalesList.size) {
                    updateState { copy(currentHotSales = currentHotSales + 1) }
                }
            } else {
                if (stateFlow.value.currentHotSales - 1 >= 0) {
                    updateState { copy(currentHotSales = currentHotSales - 1) }
                }
            }
    }

    fun onRetryClick() {
        updateData()
    }

    fun onClickSelectCategoryView(id: Int) {
        if (stateFlow.value.selectCategory != id) {
            updateState { copy(clearSelectCategory = selectCategory) }
            updateState { copy(selectCategory = id) }
        }
    }
}