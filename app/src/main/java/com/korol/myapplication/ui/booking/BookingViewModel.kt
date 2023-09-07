package com.korol.myapplication.ui.booking

import com.korol.domain.booking.BookingInteractor
import com.korol.myapplication.common.BaseViewModel
import kotlinx.coroutines.Job
import javax.inject.Inject

class BookingViewModel
@Inject constructor(
    private val bookingInteractor: BookingInteractor,
) : BaseViewModel<BookingState>(BookingState()) {
    private var job: Job? = null
}