package com.korol.myapplication.ui.booking

import com.korol.domain.booking.model.Booking

data class BookingState(
    val booking: Booking? = null,
    val dataLoading: Boolean = false,
)