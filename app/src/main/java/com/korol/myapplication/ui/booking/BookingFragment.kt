package com.korol.myapplication.ui.booking

import androidx.fragment.app.Fragment
import com.korol.myapplication.R

class BookingFragment : Fragment(R.layout.fragment_booking) {
    @javax.inject.Inject
    lateinit var vmFactory: BookingViewModelFactory
    private lateinit var viewModel: BookingViewModel
}