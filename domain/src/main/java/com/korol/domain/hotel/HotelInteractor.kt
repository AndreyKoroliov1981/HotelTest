package com.korol.domain.hotel

import com.korol.domain.hotel.model.Hotel
import com.korol.domain.hotel.model.Response

interface HotelInteractor {
    suspend fun getHotel(): Response<Hotel>
}
