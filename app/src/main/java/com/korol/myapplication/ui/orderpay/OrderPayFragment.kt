package com.korol.myapplication.ui.orderpay

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import by.kirich1409.viewbindingdelegate.viewBinding
import com.korol.myapplication.R
import com.korol.myapplication.app.App
import com.korol.myapplication.databinding.FragmentOrderPayBinding
import com.korol.myapplication.ui.hotel.HotelViewModel
import kotlinx.coroutines.launch

class OrderPayFragment : Fragment(R.layout.fragment_order_pay) {
    private val viewBinding: FragmentOrderPayBinding by viewBinding()

    @javax.inject.Inject
    lateinit var vmFactory: OrderPayViewModelFactory
    private lateinit var viewModel: OrderPayViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        App.appComponent.injectOrderPayFragment(this)
        viewModel = ViewModelProvider(this, vmFactory)[OrderPayViewModel::class.java]
        setButtonSuperb()
        setButtonBackListeners()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.stateFlow.collect {
                        viewBinding.tvTextWithNumber.text = getString(
                            R.string.textWithNumberOrder,
                            it.numberOrder.toString(),
                        )
                    }
                }
            }
        }
    }

    private fun setButtonBackListeners() {
        viewBinding.ivBack.setOnClickListener {
            Navigation.findNavController(viewBinding.root).popBackStack()
        }
    }

    private fun setButtonSuperb() {
        viewBinding.btnSuperb.setOnClickListener {
            val action = OrderPayFragmentDirections.actionFragmentOrderPayToFragmentHotel()
            Navigation.findNavController(viewBinding.root).navigate(action)
        }
    }
}