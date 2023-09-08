package com.korol.myapplication.ui.booking

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import by.kirich1409.viewbindingdelegate.viewBinding
import com.korol.myapplication.R
import com.korol.myapplication.app.App
import com.korol.myapplication.databinding.FragmentBookingBinding
import com.korol.myapplication.ui.hotel.HotelFragmentDirections

class BookingFragment : Fragment(R.layout.fragment_booking) {
    private val viewBinding: FragmentBookingBinding by viewBinding()
    @javax.inject.Inject
    lateinit var vmFactory: BookingViewModelFactory
    private lateinit var viewModel: BookingViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        App.appComponent.injectBookingFragment(this)
        viewModel = ViewModelProvider(this, vmFactory)[BookingViewModel::class.java]
        setButtonBackListeners()
        setButtonPay()
        viewBinding.inputPhone.tvHint.text = "Номер телефона"
        viewBinding.inputPhone.etText.setText("+7 (951) 555-99-00")
        viewBinding.inputEmail.tvHint.text = "Почта"
        viewBinding.inputEmail.etText.setText("korol81@bk.ru")
    }

    private fun setButtonBackListeners() {
        viewBinding.ivBack.setOnClickListener {
            Navigation.findNavController(viewBinding.root).popBackStack()
        }
    }

    private fun setButtonPay() {
        viewBinding.btnPayRoom.setOnClickListener {
            val action = BookingFragmentDirections.actionFragmentBookingToFragmentOrderPay()
            Navigation.findNavController(viewBinding.root).navigate(action)
        }
    }
}