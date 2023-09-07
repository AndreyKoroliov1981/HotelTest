package com.korol.myapplication.ui.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.korol.domain.booking.BookingInteractor
import javax.inject.Inject

class BookingViewModelFactory
@Inject constructor(
    private val bookingInteractor: BookingInteractor,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BookingViewModel(
            bookingInteractor = bookingInteractor,
        ) as T
    }
}