package com.korol.myapplication.ui.hotel

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.korol.myapplication.R
import com.korol.myapplication.app.App.Companion.appComponent
import com.korol.myapplication.common.IsErrorData
import com.korol.myapplication.databinding.FragmentHotelBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale

class HotelFragment : Fragment(R.layout.fragment_hotel) {
    private val viewBinding: FragmentHotelBinding by viewBinding()

    @javax.inject.Inject
    lateinit var vmFactory: HotelViewModelFactory
    private lateinit var viewModel: HotelViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appComponent.injectHotelFragment(this)
        viewModel = ViewModelProvider(this, vmFactory).get(HotelViewModel::class.java)
        setPullToRefresh()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.stateFlow.collect {
                        Glide.with(this@HotelFragment)
                            .load(it.hotel?.imageUrls?.get(0))
                            .centerInside()
                            .error(R.drawable.ic_error)
                            .into(viewBinding.ivPhotoHotel)
                        if (it.hotel != null) {
                            viewBinding.tvRatingDigit.text = it.hotel.rating.toString()
                            viewBinding.tvRatingName.text = it.hotel.ratingName.toString()
                            viewBinding.tvNameHotel.text = it.hotel.name
                            viewBinding.tvAddressHotel.text = it.hotel.address
                            viewBinding.tvMinimalPrice.text = this@HotelFragment.getString(
                                R.string.textForMinimalPrice,
                                String.format(
                                    Locale.FRANCE,
                                    "%,d",
                                    it.hotel.minimalPrice,
                                ),
                            )
                            if (it.hotel.aboutTheHotel?.peculiarities != null) {
                                viewBinding.cgPeculiarities.removeAllViews()
                                for (index in it.hotel.aboutTheHotel?.peculiarities!!) {
                                    val chip = Chip(viewBinding.cgPeculiarities.context)
                                    chip.text = index
                                    chip.isClickable = false
                                    chip.isCheckable = true
                                    viewBinding.cgPeculiarities.addView(chip)
                                }
                            }
                            viewBinding.tvPriceForIt.text = it.hotel.priceForIt
                            viewBinding.tvDescription.text = it.hotel.aboutTheHotel?.description
                        }
                        viewBinding.pbLoad.isVisible = it.dataLoading
                    }
                }
                launch {
                    viewModel.sideEffect.collectLatest { sideEffect ->
                        when (sideEffect) {
                            is IsErrorData -> {
                                writeError(view, sideEffect.errorMessage)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setPullToRefresh() {
        viewBinding.srRefresh.setOnRefreshListener {
            viewBinding.srRefresh.isRefreshing = true
            viewModel.refreshLoad()
            viewBinding.srRefresh.isRefreshing = false
        }
    }

    private fun writeError(view: View, error: String) {
        val snackBarView =
            Snackbar.make(view, error, Snackbar.LENGTH_LONG)
                .setTextColor(ContextCompat.getColor(requireContext(), R.color.error_text))
                .setAction("Retry") {
                    viewModel.onClickSendRequest()
                }
        val sbView = snackBarView.view
        val params = sbView.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        sbView.layoutParams = params
        snackBarView.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
        snackBarView.show()
    }
}
