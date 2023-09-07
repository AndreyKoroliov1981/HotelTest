package com.korol.repository.hotel

import com.korol.domain.hotel.HotelRepository
import com.korol.domain.hotel.model.Hotel
import com.korol.domain.Response
import com.korol.network.api.hotel.HotelRetrofitService
import com.korol.network.api.hotel.model.HotelResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HotelRepositoryImpl
@Inject constructor(
    private val hotelMapper: HotelMapper,
    private var hotelRetrofitService: HotelRetrofitService,
) : HotelRepository {
    override suspend fun getHotel(): Response<Hotel> =
        withContext(Dispatchers.IO) {
            try {
                val response = hotelRetrofitService.getHotel().execute()
                val responseBody: HotelResponse?
                val responseError: String?
                if (response.isSuccessful) {
                    responseBody = response.body()
                    var hotel: Hotel? = null
                    if (responseBody != null) { hotel = hotelMapper.mapHotelFromNetwork(responseBody) }
                    return@withContext Response(
                        data = hotel,
                        errorText = null,
                    )
                } else {
                    responseError = TEXT_NO_DATA_FROM_SERVER
                    return@withContext Response(
                        data = null,
                        errorText = responseError,
                    )
                }
            } catch (e: java.lang.Exception) {
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
