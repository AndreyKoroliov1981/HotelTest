package com.korol.repository.room

import com.korol.domain.room.model.Room
import com.korol.network.api.room.model.RoomResponse
import javax.inject.Inject

class RoomMapper
@Inject constructor() {
    fun mapRoomsFromNetwork(roomsResponse: RoomResponse): List<Room> {
        val listRoom = mutableListOf<Room>()
        for (item in roomsResponse.rooms) {
            val room = Room(
                id = item.id,
                name = item.name,
                price = item.price,
                pricePer = item.pricePer,
                peculiarities = item.peculiarities,
                imageUrls = item.imageUrls,
            )
            listRoom.add(room)
        }
        return listRoom
    }
}