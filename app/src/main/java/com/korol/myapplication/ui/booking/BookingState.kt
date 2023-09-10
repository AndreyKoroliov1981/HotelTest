package com.korol.myapplication.ui.booking

import com.korol.domain.booking.model.Booking
import com.korol.myapplication.ui.booking.model.Person

data class BookingState(
    val booking: Booking? = null,
    val fullPay: Long = 0,
    val dataLoading: Boolean = false,
    val persons: List<Person> = listOf(Person()),
    val isOpenViewPerson: List<Boolean> = listOf(false),
    val correctPhone: Boolean? = null,
    val correctFirstTourist: Boolean? = null,
    val phoneNumber: String = "",
    val correctEmail: Boolean? = null,
    val email: String = "",
    val isPayed: Boolean = false
)