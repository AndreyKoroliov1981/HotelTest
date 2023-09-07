package com.korol.network.api.booking

import com.korol.network.api.booking.model.BookingResponse
import retrofit2.Call
import retrofit2.http.GET

interface BookingRetrofitService {
    @GET("e8868481-743f-4eb2-a0d7-2bc4012275c8")
    fun getBooking(): Call<BookingResponse>
}
