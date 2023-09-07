package com.korol.domain.hotel

import com.korol.domain.Response
import com.korol.domain.hotel.model.Hotel

interface HotelInteractor {
    suspend fun getHotel(): Response<Hotel>
}
