package com.korol.myapplication.ui.hotel.model

import com.korol.domain.hotel.model.AboutTheHotel
import com.korol.domain.hotel.model.Hotel
import javax.inject.Inject

class HotelVoMapper
@Inject constructor() {
    fun mapHotelFromDomain(hotel: Hotel): HotelVo {
        return with(hotel) {
            HotelVo(
                id = id,
                name = name,
                address = address,
                minimalPrice = minimalPrice,
                priceForIt = priceForIt,
                rating = rating,
                ratingName = ratingName,
                imageUrls = imageUrls,
                aboutTheHotel = mapAboutTheHotelFromDomain(aboutTheHotel),
            )
        }
    }

    private fun mapAboutTheHotelFromDomain(aboutTheHotel: AboutTheHotel?): AboutTheHotelVo? {
        if (aboutTheHotel == null) return null
        return AboutTheHotelVo(
            description = aboutTheHotel.description,
            peculiarities = aboutTheHotel.peculiarities,
        )
    }
}
