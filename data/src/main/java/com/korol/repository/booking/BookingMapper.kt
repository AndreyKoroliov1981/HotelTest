package com.korol.repository.booking

import com.korol.domain.booking.model.Booking
import com.korol.network.api.booking.model.BookingResponse
import javax.inject.Inject

class BookingMapper
@Inject constructor(){
    fun mapBookingFromNetwork(bookingResponse: BookingResponse): Booking {
        return Booking(
             id = bookingResponse.id,
         hotelName = bookingResponse.hotelName,
         hotelAddress = bookingResponse.hotelAddress,
         horating = bookingResponse.horating,
         ratingName = bookingResponse.ratingName,
         departure = bookingResponse.departure,
         arrivalCountry = bookingResponse.arrivalCountry,
         tourDateStart = bookingResponse.tourDateStart,
         tourDateStop = bookingResponse.tourDateStop,
         numberOfNights = bookingResponse.numberOfNights,
         room = bookingResponse.room,
         nutrition = bookingResponse.nutrition,
         tourPrice = bookingResponse.tourPrice,
         fuelCharge = bookingResponse.fuelCharge,
         serviceCharge = bookingResponse.serviceCharge,
        )
    }
}