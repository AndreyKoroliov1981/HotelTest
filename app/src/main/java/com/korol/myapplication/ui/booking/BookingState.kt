package com.korol.myapplication.ui.booking

import com.korol.domain.booking.model.Booking
import com.korol.myapplication.ui.booking.model.Person

data class BookingState(
    val booking: Booking? = null,
    val fullPay: Long = 0,
    val dataLoading: Boolean = false,
    val persons: List<Person> = listOf(Person()),
    val isOpenViewPerson: List<Boolean> = listOf(false),
)