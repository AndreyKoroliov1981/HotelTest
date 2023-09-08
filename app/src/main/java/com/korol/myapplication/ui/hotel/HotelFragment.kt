package com.korol.myapplication.ui.hotel

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.GestureDetector
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
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
import kotlin.math.abs

class HotelFragment : Fragment(R.layout.fragment_hotel) {
    private val viewBinding: FragmentHotelBinding by viewBinding()

    @javax.inject.Inject
    lateinit var vmFactory: HotelViewModelFactory
    private lateinit var viewModel: HotelViewModel

    // Animations
    private var slideInLeft: Animation? = null
    private var slideOutRight: Animation? = null
    private var slideInRight: Animation? = null
    private var slideOutLeft: Animation? = null
    private var overscrollLeftFadeOut: Animation? = null
    private var overscrollRightFadeOut: Animation? = null

    private lateinit var overscrollLeft: View
    private lateinit var overscrollRight: View

    private val sliderDotsPanel: MutableList<ImageView> = mutableListOf()

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appComponent.injectHotelFragment(this)
        viewModel = ViewModelProvider(this, vmFactory)[HotelViewModel::class.java]
        setPullToRefresh()
        setButtonChoice()
        val gestureDetector = GestureDetector(requireContext(), SwipeListener())
        viewBinding.isPhotoHotel.setOnTouchListener { v, event ->
            gestureDetector.onTouchEvent(event)
            v.performClick()
            true
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.stateFlow.collect {
                        Glide.with(this@HotelFragment)
                            .load(it.hotel?.imageUrls?.get(it.currentPhoto))
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
                                    chip.chipBackgroundColor =
                                        resources.getColorStateList(R.color.color_background_addInformation, null)
                                    chip.setTextColor(resources.getColorStateList(R.color.gray, null))
                                    chip.isClickable = false
                                    chip.isCheckable = true
                                    viewBinding.cgPeculiarities.addView(chip)
                                }
                            }
                            viewBinding.tvPriceForIt.text = it.hotel.priceForIt
                            viewBinding.tvDescription.text = it.hotel.aboutTheHotel?.description
                            if (sliderDotsPanel == emptyList<ImageView>()) {
                                createSliderDotsPanel(it.currentPhoto, it.hotel.imageUrls?.size ?: 0)
                            } else {
                                updateSliderDotsPanel(it.currentPhoto, it.hotel.imageUrls?.size ?: 0)
                            }
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

    private fun createSliderDotsPanel(currentPosition: Int, size: Int) {
        for (i in 0 until size) {
            val newDot = ImageView(this.context)
            if (i != currentPosition) {
                newDot.setImageDrawable(
                    ContextCompat.getDrawable(
                        this.requireContext(),
                        R.drawable.non_active_dot,
                    ),
                )
            } else {
                newDot.setImageDrawable(
                    ContextCompat.getDrawable(
                        this.requireContext(),
                        R.drawable.active_dot,
                    ),
                )
            }
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
            )
            when (i) {
                0 -> params.setMargins(15, 8, 5, 8)
                size - 1 -> params.setMargins(5, 8, 15, 8)
                else -> params.setMargins(5, 8, 5, 8)
            }
            viewBinding.sdpPhotoHotel.llSliderDotsPanel.addView(newDot, params)
            sliderDotsPanel.add(newDot)
        }
    }

    private fun updateSliderDotsPanel(currentPosition: Int, size: Int) {
        for (i in 0 until size) {
            if (i != currentPosition) {
                sliderDotsPanel[i].setImageDrawable(
                    ContextCompat.getDrawable(
                        this.requireContext(),
                        R.drawable.non_active_dot,
                    ),
                )
            } else {
                sliderDotsPanel[i].setImageDrawable(
                    ContextCompat.getDrawable(
                        this.requireContext(),
                        R.drawable.active_dot,
                    ),
                )
            }
        }
    }

    private fun setButtonChoice() {
        viewBinding.btnChoiceRoom.setOnClickListener {
            val action = HotelFragmentDirections.actionFragmentHotelToFragmentRoom(
                viewModel.stateFlow.value.hotel?.name ?: "",
            )
            Navigation.findNavController(viewBinding.root).navigate(action)
        }
    }

    private fun setPullToRefresh() {
        viewBinding.srRefresh.setOnRefreshListener {
            viewBinding.srRefresh.isRefreshing = true
            viewModel.refreshLoad()
            viewBinding.srRefresh.isRefreshing = false
        }
    }

    private enum class Direction { LEFT, RIGHT }

    private fun moveNextOrPrevious(delta: Int) {
        if (viewModel.stateFlow.value.hotel?.imageUrls != emptyList<String>()) {
            if (delta > 0) setupAnimations(Direction.RIGHT)
            if (delta < 0) setupAnimations(Direction.LEFT)
            viewModel.onSwipe(delta)
        }
    }

    private fun setupAnimations(direction: Direction) {
        // Swipe animations
        viewBinding.isPhotoHotel.inAnimation = when (direction) {
            Direction.RIGHT -> slideInRight
            Direction.LEFT -> slideInLeft
        }
        viewBinding.isPhotoHotel.outAnimation = when (direction) {
            Direction.RIGHT -> slideOutLeft
            Direction.LEFT -> slideOutRight
        }
        // overscroll (no more photos) effect on the side on the screen
        if (viewModel.stateFlow.value.hotel?.imageUrls != null) {
            if (viewModel.stateFlow.value.hotel?.imageUrls!!.size <= 1) {
                when (direction) {
                    Direction.RIGHT -> {
                        overscrollRight.visibility = View.VISIBLE
                        overscrollRight.startAnimation(overscrollRightFadeOut)
                        return
                    }

                    Direction.LEFT -> {
                        overscrollLeft.visibility = View.VISIBLE
                        overscrollLeft.startAnimation(overscrollLeftFadeOut)
                        return
                    }
                }
            }
        }
    }

    private inner class SwipeListener : GestureDetector.SimpleOnGestureListener() {
        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float,
        ): Boolean {
            /* Swipe parameters */
            val swipeMinDistance = 75
            val swipeMaxOffPath = 250
            val swipeThresholdVelocity = 200
            if (abs(e1.y - e2.y) > swipeMaxOffPath) {
                return false
            }
            // right to left swipe
            if (e1.x - e2.x > swipeMinDistance && abs(velocityX) > swipeThresholdVelocity) {
                moveNextOrPrevious(1)
            } else if (e2.x - e1.x > swipeMinDistance && abs(velocityX) > swipeThresholdVelocity) {
                moveNextOrPrevious(-1)
            }
            return false
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
