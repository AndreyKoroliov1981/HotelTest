package com.korol.myapplication.ui.filters

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.korol.myapplication.R
import com.korol.myapplication.databinding.FragmentFiltersBinding

class FiltersFragment : BottomSheetDialogFragment(R.layout.fragment_filters) {
    private val viewBinding: FragmentFiltersBinding by viewBinding()

    private val brandsList = listOf("Apple", "Sumsung", "Xiaomi")
    private val priceList = listOf("$0 - $300", "$300 - $500", "$500 - $1000", "$1 000 - $10 000")
    private val sizeList = listOf("4.5 to 5.5 inches", "5.5 to 6.5 inches")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setBrandSpinner()
        setPriceSpinner()
        setSizeSpinner()
        viewBinding.btnClose.setOnClickListener {
            dismiss()
        }
        viewBinding.btnDone.setOnClickListener {
            dismiss()
        }
    }

    private fun setSizeSpinner(){
        val adapterSize = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            sizeList
        )
        viewBinding.spinnerSize.setAdapter(adapterSize)
        viewBinding.spinnerSize.setOnClickListener {
        }
    }

    private fun setPriceSpinner(){
        val adapterPrice = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            priceList
        )
        viewBinding.spinnerPrice.setAdapter(adapterPrice)
        viewBinding.spinnerPrice.setOnClickListener {
        }
    }

    private fun setBrandSpinner() {
        val adapterBrand = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            brandsList
        )
        viewBinding.spinnerBrand.setAdapter(adapterBrand)
        viewBinding.spinnerBrand.setOnClickListener {
        }
    }
}