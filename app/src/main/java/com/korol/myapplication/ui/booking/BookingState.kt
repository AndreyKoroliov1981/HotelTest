package com.korol.myapplication.ui.booking

import com.korol.domain.booking.model.Booking

data class BookingState(
    val hotel: Booking? = null,
    val dataLoading: Boolean = false,
)