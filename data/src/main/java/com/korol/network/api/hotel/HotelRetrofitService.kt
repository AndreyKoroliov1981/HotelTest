package com.korol.network.api.hotel

import com.korol.network.api.hotel.model.HotelResponse
import retrofit2.Call
import retrofit2.http.GET

interface HotelRetrofitService {
    @GET("35e0d18e-2521-4f1b-a575-f0fe366f66e3")
    fun getHotel(): Call<HotelResponse>
}
