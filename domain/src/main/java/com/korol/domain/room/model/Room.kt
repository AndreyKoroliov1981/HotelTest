package com.korol.domain.room.model

data class Room(
    val id: Long,
    val name: String,
    val price: Long,
    val pricePer: String,
    val peculiarities: List<String>?,
    val imageUrls: List<String>?,
)
