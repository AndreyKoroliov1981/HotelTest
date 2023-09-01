package com.korol.repository.hotel

import com.korol.domain.hotel.model.AboutTheHotel
import com.korol.domain.hotel.model.Hotel
import com.korol.network.api.hotel.model.AboutTheHotelResponse
import com.korol.network.api.hotel.model.HotelResponse
import javax.inject.Inject

class HotelMapper
@Inject constructor() {
    fun mapHotelFromNetwork(hotelResponse: HotelResponse): Hotel {
        return Hotel(
            id = hotelResponse.id,
            name = hotelResponse.name,
            address = hotelResponse.address,
            minimalPrice = hotelResponse.minimalPrice,
            priceForIt = hotelResponse.priceForIt,
            rating = hotelResponse.rating,
            ratingName = hotelResponse.ratingName,
            imageUrls = hotelResponse.imageUrls,
            aboutTheHotel = mapAboutTheHotelFromNetwork(hotelResponse.aboutTheHotel),
        )
    }

    private fun mapAboutTheHotelFromNetwork(aboutTheHotelResponse: AboutTheHotelResponse?): AboutTheHotel? {
        if (aboutTheHotelResponse == null) return null
        return AboutTheHotel(
            description = aboutTheHotelResponse.description,
            peculiarities = aboutTheHotelResponse.peculiarities,
        )
    }
}
