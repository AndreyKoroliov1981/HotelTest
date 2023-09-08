package com.korol.network.api.booking.model

import com.google.gson.annotations.SerializedName

data class BookingResponse(
    val id: Long,
    @SerializedName("hotel_name")val hotelName: String,
    @SerializedName("hotel_adress") val hotelAddress: String,
    val horating: Long,
    @SerializedName("rating_name") val ratingName: String,
    val departure: String,
    @SerializedName("arrival_country") val arrivalCountry: String,
    @SerializedName("tour_date_start") val tourDateStart: String,
    @SerializedName("tour_date_stop") val tourDateStop: String,
    @SerializedName("number_of_nights") val numberOfNights: Int,
    val room: String,
    val nutrition: String,
    @SerializedName("tour_price") val tourPrice: Long?,
    @SerializedName("fuel_charge") val fuelCharge: Long?,
    @SerializedName("service_charge") val serviceCharge: Long?,
)