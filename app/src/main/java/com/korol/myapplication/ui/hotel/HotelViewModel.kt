package com.korol.myapplication.ui.hotel

import com.korol.domain.hotel.HotelInteractor
import com.korol.domain.hotel.model.Hotel
import com.korol.myapplication.common.BaseViewModel
import com.korol.myapplication.common.IsErrorData
import com.korol.myapplication.ui.hotel.model.HotelVo
import com.korol.myapplication.ui.hotel.model.HotelVoMapper
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import javax.inject.Inject

const val DEBOUNCE_MILS = 300L

class HotelViewModel
@Inject constructor(
    private val hotelInteractor: HotelInteractor,
    private val hotelVoMapper: HotelVoMapper
) : BaseViewModel<HotelState>(HotelState()) {

    private var job: Job? = null

    init {
        refreshLoad()
    }

    fun refreshLoad() {
        updateState { copy(hotel = null) }
        getHotel()
    }

    fun mapHotelToHotelVo(hotel: Hotel): HotelVo {
        return hotelVoMapper.mapHotelFromDomain(hotel)
    }

    private fun getHotel() {
        job?.cancel()
        job = launch {
            delay(DEBOUNCE_MILS)
            updateState { copy(dataLoading = true) }

            val responseHotel = hotelInteractor.getHotel()

            if (responseHotel.errorText == null) {
                updateState { copy(hotel = responseHotel.data) }
            } else {
                if (responseHotel.data != null) {
                    updateState { copy(hotel = responseHotel.data) }
                }
                sideEffectSharedFlow.emit(IsErrorData(responseHotel.errorText!!))
            }

            updateState { copy(dataLoading = false) }
        }
    }

    fun onClickSendRequest() {
        getHotel()
    }
}
