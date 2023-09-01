package com.korol.network.api.hotel.model

import com.google.gson.annotations.SerializedName

data class HotelResponse(
    val id: Long,
    val name: String,
    @SerializedName("adress") val address: String,
    @SerializedName("minimal_price") val minimalPrice: Long,
    @SerializedName("price_for_it") val priceForIt: String,
    val rating: Int?,
    @SerializedName("rating_name") val ratingName: String?,
    @SerializedName("image_urls") val imageUrls: List<String>?,
    @SerializedName("about_the_hotel") val aboutTheHotel: AboutTheHotelResponse?,
)

data class AboutTheHotelResponse(
    val description: String?,
    val peculiarities: List<String>?,
)
