package com.korol.network.api.room.model

import com.google.gson.annotations.SerializedName

data class RoomResponse(
    val rooms: List<SingleRoomResponse>,
)

data class SingleRoomResponse(
    val id: Long,
    val name: String,
    val price: Long,
    @SerializedName("price_per") val pricePer: String,
    val peculiarities: List<String>?,
    @SerializedName("image_urls") val imageUrls: List<String>?,
)
