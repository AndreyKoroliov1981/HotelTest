package com.korol.myapplication.ui.hotel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.korol.domain.hotel.HotelInteractor
import com.korol.myapplication.ui.hotel.model.HotelVoMapper
import javax.inject.Inject

class HotelViewModelFactory
@Inject constructor(
    private val hotelInteractor: HotelInteractor,
    private val hotelVoMapper: HotelVoMapper,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HotelViewModel(
            hotelInteractor = hotelInteractor,
            hotelVoMapper = hotelVoMapper,
        ) as T
    }
}
