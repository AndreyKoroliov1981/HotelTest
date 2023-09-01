package com.korol.myapplication.ui.hotel.model

data class HotelVo(
    val id: Long,
    val name: String,
    val address: String,
    val minimalPrice: Long,
    val priceForIt: String,
    val rating: Int?,
    val ratingName: String?,
    val imageUrls: List<String>?,
    val aboutTheHotel: AboutTheHotelVo?,
)

data class AboutTheHotelVo(
    val description: String?,
    val peculiarities: List<String>?,
)
