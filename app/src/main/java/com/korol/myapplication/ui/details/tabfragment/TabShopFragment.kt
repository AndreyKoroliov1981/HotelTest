package com.korol.myapplication.ui.details.tabfragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.korol.myapplication.R
import com.korol.myapplication.databinding.ItemShopDetailsFragmentBinding
import com.korol.myapplication.ui.details.DetailsFragment.Companion.KEY_FOR_TAG_SHOP
import com.korol.myapplication.ui.details.DetailsFragment.Companion.TAG_SHOP

class TabShopFragment: Fragment(R.layout.item_shop_details_fragment) {
    private val viewBinding: ItemShopDetailsFragmentBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentListeners()
    }

    private fun setFragmentListeners() {
        parentFragmentManager.setFragmentResultListener(
            TAG_SHOP, viewLifecycleOwner
        ) { _, bundle ->
            val data = bundle.getStringArray(KEY_FOR_TAG_SHOP)
            if (data != null) {
                viewBinding.tvCPU.text = data[0]
                viewBinding.tvCamera.text = data[1]
                viewBinding.tvSd.text = data[2]
                viewBinding.tvSsd.text = data[3]
            }
        }
    }
}