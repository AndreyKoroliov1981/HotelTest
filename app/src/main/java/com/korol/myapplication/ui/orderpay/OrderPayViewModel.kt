package com.korol.myapplication.ui.orderpay

import com.korol.myapplication.common.BaseViewModel

class OrderPayViewModel : BaseViewModel<OrderPayState>(OrderPayState()) {
    init {
        val number = (100_000..999_000).random().toLong()
        updateState { copy(numberOrder = number) }
    }
}