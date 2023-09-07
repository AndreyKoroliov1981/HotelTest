package com.korol.repository.booking

import com.korol.domain.Response
import com.korol.domain.booking.BookingRepository
import com.korol.domain.booking.model.Booking
import com.korol.network.api.booking.BookingRetrofitService
import com.korol.network.api.booking.model.BookingResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BookingRepositoryImpl @Inject constructor(
    private val bookingMapper: BookingMapper,
    private var bookingRetrofitService: BookingRetrofitService,
) : BookingRepository {
    override suspend fun getBooking(): Response<Booking> =
        withContext(Dispatchers.IO) {
            try {
                val response = bookingRetrofitService.getBooking().execute()
                val responseBody: BookingResponse?
                val responseError: String?
                if (response.isSuccessful) {
                    responseBody = response.body()
                    var booking: Booking? = null
                    if (responseBody != null) { booking = bookingMapper.mapBookingFromNetwork(responseBody) }
                    return@withContext Response(
                        data = booking,
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