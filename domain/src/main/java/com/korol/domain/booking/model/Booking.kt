package com.korol.domain.booking.model

data class Booking(
    val id: Long,
    val hotelName: String,
    val hotelAddress: String,
    val horating: Long,
    val ratingName: String,
    val departure: String,
    val arrivalCountry: String,
    val tourDateStart: String,
    val tourDateStop: String,
    val numberOfNights: Int,
    val room: String,
    val nutrition: String,
    val tourPrice: Long,
    val fuelCharge: Long,
    val serviceCharge: Long,
)
