package com.korol.myapplication.ui.booking

import com.korol.domain.booking.BookingInteractor
import com.korol.myapplication.common.BaseViewModel
import com.korol.myapplication.common.IsErrorData
import com.korol.myapplication.ui.hotel.DEBOUNCE_MILS
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import javax.inject.Inject

class BookingViewModel
@Inject constructor(
    private val bookingInteractor: BookingInteractor,
) : BaseViewModel<BookingState>(BookingState()) {
    private var job: Job? = null
    init {
        refreshLoad()
    }

    fun refreshLoad() {
        updateState { copy(booking = null) }
        getBooking()
    }

    private fun getBooking() {
        job?.cancel()
        job = launch {
            delay(DEBOUNCE_MILS)
            updateState { copy(dataLoading = true) }

            val responseBooking = bookingInteractor.getBooking()

            if (responseBooking.errorText == null) {
                updateState { copy(booking = responseBooking.data) }
            } else {
                if (responseBooking.data != null) {
                    updateState { copy(booking = responseBooking.data) }
                }
                sideEffectSharedFlow.emit(IsErrorData(responseBooking.errorText!!))
            }

            updateState { copy(dataLoading = false) }
        }
    }
    fun onClickSendRequest() {
        getBooking()
    }
}