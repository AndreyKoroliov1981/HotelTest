package com.korol.network.api.room

import com.korol.network.api.room.model.RoomResponse
import retrofit2.Call
import retrofit2.http.GET

interface RoomRetrofitService {
    @GET("f9a38183-6f95-43aa-853a-9c83cbb05ecd")
    fun getRooms(): Call<RoomResponse>
}