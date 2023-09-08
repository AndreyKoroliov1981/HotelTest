package com.korol.myapplication.ui.orderpay

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import by.kirich1409.viewbindingdelegate.viewBinding
import com.korol.myapplication.R
import com.korol.myapplication.app.App
import com.korol.myapplication.databinding.FragmentOrderPayBinding

class OrderPayFragment : Fragment(R.layout.fragment_order_pay) {
    private val viewBinding: FragmentOrderPayBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        App.appComponent.injectOrderPayFragment(this)
        setButtonSuperb()
        setButtonBackListeners()
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