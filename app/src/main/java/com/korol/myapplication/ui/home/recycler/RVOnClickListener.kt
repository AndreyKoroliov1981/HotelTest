package com.korol.myapplication.ui.home.recycler

import com.korol.network.api.home.model.BestSeller

interface RVOnClickListener {
    fun onClicked(item: BestSeller)
}