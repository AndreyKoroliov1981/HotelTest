package com.korol.domain.booking

import com.korol.domain.Response
import com.korol.domain.booking.model.Booking
import javax.inject.Inject

class BookingInteractorImpl
@Inject constructor(
    private val bookingRepository: BookingRepository,
) : BookingInteractor {
    override suspend fun getBooking(): Response<Booking> {
        return bookingRepository.getBooking()
    }
}