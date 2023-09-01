package com.korol.myapplication.ui.home

import com.korol.network.api.home.model.BestSeller
import com.korol.network.api.home.model.HomeStore

data class HomeState(
    val clearSelectCategory: Int = -1,
    val selectCategory: Int = 0,
    val hotSalesList: List<HomeStore> = emptyList(),
    val currentHotSales: Int = 0,
    val bestSellerList: List<BestSeller> = emptyList(),
)
