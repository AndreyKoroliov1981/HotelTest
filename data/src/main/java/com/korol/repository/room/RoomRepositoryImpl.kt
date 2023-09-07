package com.korol.repository.room

import com.korol.domain.Response
import com.korol.domain.room.RoomRepository
import com.korol.domain.room.model.Room
import com.korol.network.api.room.RoomRetrofitService
import com.korol.network.api.room.model.RoomResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RoomRepositoryImpl @Inject constructor(
    private val roomMapper: RoomMapper,
    private var roomRetrofitService: RoomRetrofitService,
) : RoomRepository {
    override suspend fun getRooms(): Response<List<Room>> =
        withContext(Dispatchers.IO) {
            try {
                val response = roomRetrofitService.getRooms().execute()
                val responseBody: RoomResponse?
                val responseError: String?
                if (response.isSuccessful) {
                    responseBody = response.body()
                    var rooms: List<Room>? = null
                    if (responseBody != null) { rooms = roomMapper.mapRoomsFromNetwork(responseBody) }
                    return@withContext Response(
                        data = rooms,
                        errorText = null,
                    )
                } else {
                    responseError = TEXT_NO_DATA_FROM_SERVER
                    return@withContext Response(
                        data = null,
                        errorText = responseError,
                    )
                }
            } catch (e: Exception) {
                return@withContext Response(
                    data = null,
                    errorText = e.toString(),
                )
            }
        }

    companion object {
        const val TEXT_NO_DATA_FROM_SERVER = "No data from server"
    }
}