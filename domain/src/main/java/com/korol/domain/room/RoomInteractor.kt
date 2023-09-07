package com.korol.domain.room

import com.korol.domain.Response
import com.korol.domain.room.model.Room

interface RoomInteractor {
    suspend fun getRooms(): Response<List<Room>>
}