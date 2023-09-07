package com.korol.domain.hotel

import com.korol.domain.Response
import com.korol.domain.hotel.model.Hotel
import javax.inject.Inject

class HotelInteractorImpl
@Inject constructor(
    private val hotelRepository: HotelRepository,
) : HotelInteractor {
    override suspend fun getHotel(): Response<Hotel> {
        return hotelRepository.getHotel()
    }
}
