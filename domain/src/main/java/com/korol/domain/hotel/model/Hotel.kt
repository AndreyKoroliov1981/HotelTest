package com.korol.domain.hotel.model

data class Hotel(
    val id: Long,
    val name: String,
    val address: String,
    val minimalPrice: Long,
    val priceForIt: String,
    val rating: Int?,
    val ratingName: String?,
    val imageUrls: List<String>?,
    val aboutTheHotel: AboutTheHotel?,
)

data class AboutTheHotel(
    val description: String?,
    val peculiarities: List<String>?,
)
