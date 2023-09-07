package com.korol.domain.booking

import com.korol.domain.Response
import com.korol.domain.booking.model.Booking

interface BookingRepository {
    suspend fun getBooking(): Response<Booking>
}