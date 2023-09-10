package com.korol.myapplication.ui.booking

import android.text.TextUtils
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
            updateState { copy(correctPhone = false) }
        } else {
            updateState { copy(correctPhone = true) }
        }
    }

    private fun cropPhone(inputPhone: String): String {
        var cropPhone = ""
        for (i in inputPhone) if (i.isDigit()) cropPhone += i
        return cropPhone
    }

    fun checkEmail(email: String) {
        if (!TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            updateState { copy(correctEmail = true) }
        } else {
            updateState { copy(correctEmail = false) }
        }
    }

    fun onClickFirstTouristArrow() {
        val newList: MutableList<Boolean> = state.isOpenViewPerson.toMutableList()
        val state = state.isOpenViewPerson[0]
        newList[0] = !state
        updateState { copy(isOpenViewPerson = newList) }
    }

    fun onClickButtonPay() {
        updateState { copy(correctFirstTourist = checkTourist()) }
        val checkIsOk = state.correctEmail == true && state.correctPhone == true &&
            state.correctFirstTourist == true
        if (checkIsOk) {
            updateState { copy(isPayed = true) }
            updateState { copy(isPayed = false) } // для возможности последующего возврата на экран
        } else {
            if (state.correctEmail == null) updateState { copy(correctEmail = false) }
            if (state.correctPhone == null) updateState { copy(correctPhone = false) }
            if (state.correctFirstTourist == null) updateState { copy(correctFirstTourist = false) }
        }
    }

    private fun checkTourist(): Boolean? {
        if ((state.persons[0].name == null) &&
            (state.persons[0].surname == null) &&
            (state.persons[0].birthday == null) &&
            (state.persons[0].citizenship == null) &&
            (state.persons[0].numberPassport == null) &&
            (state.persons[0].validityPeriodPassport == null)
        ) {
            return null
        }

        if ((state.persons[0].name?.length ?: 0) >= TEXT_LENGTH &&
            (state.persons[0].surname?.length ?: 0) >= TEXT_LENGTH &&
            (state.persons[0].birthday?.length ?: 0) >= TEXT_LENGTH &&
            (state.persons[0].citizenship?.length ?: 0) >= TEXT_LENGTH &&
            (state.persons[0].numberPassport?.length ?: 0) >= TEXT_LENGTH &&
            (state.persons[0].validityPeriodPassport?.length ?: 0) >= TEXT_LENGTH
        ) {
            return true
        }
        return false
    }

    fun updatePhone(textPhone: String) {
        updateState { copy(phoneNumber = cropPhone(textPhone)) }
    }

    fun updateEmail(textEmail: String) {
        updateState { copy(email = textEmail) }
    }

    fun updateFirstTouristName(text: String) {
        val person = state.persons[0].copy(name = text)
        updateState { copy(persons = listOf(person)) }
    }

    fun updateFirstTouristSurname(text: String) {
        val person = state.persons[0].copy(surname = text)
        updateState { copy(persons = listOf(person)) }
    }

    fun updateFirstTouristBirthday(text: String) {
        val person = state.persons[0].copy(birthday = text)
        updateState { copy(persons = listOf(person)) }
    }

    fun updateFirstTouristCitizenship(text: String) {
        val person = state.persons[0].copy(citizenship = text)
        updateState { copy(persons = listOf(person)) }
    }

    fun updateFirstTouristNumberPassport(text: String) {
        val person = state.persons[0].copy(numberPassport = text)
        updateState { copy(persons = listOf(person)) }
    }

    fun updateFirstTouristValidityPeriodPassport(text: String) {
        val person = state.persons[0].copy(validityPeriodPassport = text)
        updateState { copy(persons = listOf(person)) }
    }

    companion object {
        const val PHONE_LENGTH = 11
        const val TEXT_LENGTH = 1
    }
}