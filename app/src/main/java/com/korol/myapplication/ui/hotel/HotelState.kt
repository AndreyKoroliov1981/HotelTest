package com.korol.myapplication.ui.hotel

import com.korol.domain.hotel.model.Hotel

data class HotelState(
    val hotel: Hotel? = null,
    val dataLoading: Boolean = false
)
