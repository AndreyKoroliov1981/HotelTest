package com.korol.myapplication.ui.booking

import android.text.TextUtils
import android.util.Log
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
                val fullPay = if (responseBooking.data != null) {
                    responseBooking.data!!.tourPrice +
                        responseBooking.data!!.fuelCharge +
                        responseBooking.data!!.serviceCharge
                } else {
                    0
                }
                updateState { copy(booking = responseBooking.data, fullPay = fullPay) }
            } else {
                if (responseBooking.data != null) {
                    val fullPay = responseBooking.data!!.tourPrice +
                        responseBooking.data!!.fuelCharge +
                        responseBooking.data!!.serviceCharge
                    updateState { copy(booking = responseBooking.data, fullPay = fullPay) }
                }
                sideEffectSharedFlow.emit(IsErrorData(responseBooking.errorText!!))
            }

            updateState { copy(dataLoading = false) }
        }
    }
    fun onClickSendRequest() {
        getBooking()
    }

    fun checkPhoneNumber(phoneNumber: String) {
        if (cropPhone(phoneNumber).length != PHONE_LENGTH) {
            updateState { copy(incorrectPhone = false) }
        } else {
            updateState { copy(incorrectPhone = true) }
        }
    }

    private fun cropPhone(inputPhone: String): String {
        var cropPhone = ""
        for (i in inputPhone) if (i.isDigit()) cropPhone += i
        return cropPhone
    }

    fun checkEmail(email: String) {
        if (!TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            updateState { copy(incorrectEmail = true) }
        } else {
            updateState { copy(incorrectEmail = false) }
        }
    }

    fun onClickFirstTouristArrow() {
        val newList: MutableList<Boolean> = state.isOpenViewPerson.toMutableList()
        val state = state.isOpenViewPerson[0]
        newList[0] = !state
        updateState { copy(isOpenViewPerson = newList) }
    }

    companion object {
        const val PHONE_LENGTH = 11
    }
}