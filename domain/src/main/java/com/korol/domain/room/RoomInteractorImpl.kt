package com.korol.domain.room

import com.korol.domain.Response
import com.korol.domain.room.model.Room
import javax.inject.Inject

class RoomInteractorImpl
@Inject constructor(
    private val roomRepository: RoomRepository,
) : RoomInteractor {
    override suspend fun getRooms(): Response<List<Room>> {
        return roomRepository.getRooms()
    }
}