package com.korol.myapplication.ui.details

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.GestureDetector
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.korol.myapplication.R
import com.korol.myapplication.common.IsNotDetailsData
import com.korol.myapplication.databinding.FragmentDetailsBinding
import com.korol.myapplication.ui.details.tabfragment.TabDetailsFragment
import com.korol.myapplication.ui.details.tabfragment.TabFeaturesFragment
import com.korol.myapplication.ui.details.tabfragment.TabShopFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

class DetailsFragment : Fragment(R.layout.fragment_details) {
    private val viewBinding: FragmentDetailsBinding by viewBinding()

    //private val args: DetailsFragmentArgs by navArgs()

    // Animations
    private var slideInLeft: Animation? = null
    private var slideOutRight: Animation? = null
    private var slideInRight: Animation? = null
    private var slideOutLeft: Animation? = null
    private var overscrollLeftFadeOut: Animation? = null
    private var overscrollRightFadeOut: Animation? = null

    private lateinit var overscrollLeft: View
    private lateinit var overscrollRight: View

    @javax.inject.Inject
    lateinit var vmFactory: DetailsViewModel.DetailsViewModelFactory

//    private val viewModel: DetailsViewModel by viewModels {
//        DetailsViewModel.providesFactory(
//            assistedFactory = vmFactory,
//            productId = args.productId,
//        )
//    }

    private val viewModel: DetailsViewModel?= null

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // (activity?.applicationContext as App).appComponent.injectDetailsFragment(this)
        viewModel?.onCreateUpdate()
        setColorsClickListeners()
        setMemoryClickListeners()
        setButtonBackListeners()
        setButtonCartListeners()
        setButtonAddToCartListeners()
        val gestureDetector = GestureDetector(requireContext(), SwipeListener())
        viewBinding.isHotSales.setOnTouchListener { v, event ->
            gestureDetector.onTouchEvent(event)
            v.performClick()
            true
        }
        viewBinding.apply {
            lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    launch {
                        viewModel?.stateFlow?.collect {
                            if (it.detailsInfo != null) {
                                Glide.with(requireActivity())
                                    .load(it.detailsInfo.images[it.currentImage])
                                    .placeholder(R.drawable.ic_downloading)
                                    .error(R.drawable.ic_error).into(ivHotSales)
                                btnFavourites.isVisible = it.detailsInfo.isFavorites
                                tvNameProduct.text = it.detailsInfo.title
                                showRating(it.detailsInfo.rating)
                                if (!it.initPager) {
                                    showPager(
                                        arrayOf(
                                            it.detailsInfo.cpu,
                                            it.detailsInfo.camera,
                                            it.detailsInfo.sd,
                                            it.detailsInfo.ssd,
                                        ),
                                    )
                                    showColor(it.detailsInfo.color)
                                    showMemorySize(it.detailsInfo.capacity)
                                    btnAddToCart.text = getString(R.string.textBtnAdd, it.detailsInfo.price)
                                }
                                showCountProductInCart(it.countProductsInCart)
                                showChoiceColor(it.currentColor)
                                showChoiceMemorySize(it.currentMemorySize)
                            }
                        }
                    }

                    launch {
                        viewModel?.sideEffect?.collectLatest {
                            if (it is IsNotDetailsData) {
                                writeError(view, it.errorMessage)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setButtonAddToCartListeners() {
        viewBinding.btnAddToCart.setOnClickListener {
            viewModel?.onButtonAddToCartClick()
        }
    }

    private fun setButtonBackListeners() {
        viewBinding.btnBack.setOnClickListener {
//            val action = DetailsFragmentDirections.actionFragmentDetailsToFragmentHome()
//            Navigation.findNavController(viewBinding.root).navigate(action)
        }
    }

    private fun setButtonCartListeners() {
        viewBinding.btnCart.setOnClickListener {
//            val action = DetailsFragmentDirections.actionFragmentDetailsToFragmentCard()
//            Navigation.findNavController(viewBinding.root).navigate(action)
        }
    }

    private fun setColorsClickListeners() {
        viewBinding.vColor1.setOnClickListener {
            viewModel?.onColorClick(0)
        }
        viewBinding.vColor2.setOnClickListener {
            viewModel?.onColorClick(1)
        }
    }

    private fun setMemoryClickListeners() {
        viewBinding.cvMemory1.setOnClickListener {
            viewModel?.onMemoryClick(0)
        }
        viewBinding.cvMemory2.setOnClickListener {
            viewModel?.onMemoryClick(1)
        }
    }

    private fun showCountProductInCart(count: Int) {
        if (count != 0) {
            viewBinding.tvCountProductInCart.isVisible = true
            viewBinding.tvCountProductInCart.text = count.toString()
        } else {
            viewBinding.tvCountProductInCart.isVisible = false
        }
    }

    private fun showColor(colorList: List<String>) {
        if (viewModel?.stateFlow?.value?.detailsInfo != null) {
            for (i in 0 until colorList.size) {
                when (i) {
                    0 -> viewBinding.vColor1.setBackgroundTintList(
                        ColorStateList.valueOf(colorList[i].toColorInt()),
                    )
                    1 -> viewBinding.vColor2.setBackgroundTintList(
                        ColorStateList.valueOf(colorList[i].toColorInt()),
                    )
                }
            }
        }
    }

    private fun showChoiceColor(number: Int) {
        when (number) {
            0 -> {
                viewBinding.ivChoiceColor1.isVisible = true
                viewBinding.ivChoiceColor2.isVisible = false
            }
            1 -> {
                viewBinding.ivChoiceColor1.isVisible = false
                viewBinding.ivChoiceColor2.isVisible = true
            }
        }
    }

    private fun showMemorySize(memoryList: List<String>) {
        if (viewModel?.stateFlow?.value?.detailsInfo != null) {
            for (i in 0 until memoryList.size) {
                when (i) {
                    0 -> viewBinding.tvMemory1.text = getString(R.string.sizeMemory, memoryList[i])
                    1 -> viewBinding.tvMemory2.text = getString(R.string.sizeMemory, memoryList[i])
                }
            }
        }
    }

    private fun showChoiceMemorySize(number: Int) {
        when (number) {
            0 -> {
                viewBinding.cvMemory1.setBackgroundTintList(
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.orange,
                        ),
                    ),
                )
                viewBinding.cvMemory2.setBackgroundTintList(
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white,
                        ),
                    ),
                )
                viewBinding.tvMemory1.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white,
                    ),
                )
                viewBinding.tvMemory2.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.size_memory,
                    ),
                )
            }
            1 -> {
                viewBinding.cvMemory1.setBackgroundTintList(
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white,
                        ),
                    ),
                )
                viewBinding.cvMemory2.setBackgroundTintList(
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.orange,
                        ),
                    ),
                )
                viewBinding.tvMemory1.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.size_memory,
                    ),
                )
                viewBinding.tvMemory2.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white,
                    ),
                )
            }
        }
    }

    private fun showPager(list: Array<String>) {
        val adapter = DetailsPageAdapter(this@DetailsFragment)
        adapter.addFragment(TabShopFragment(), "Shop")
        adapter.addFragment(TabDetailsFragment(), "Details")
        adapter.addFragment(TabFeaturesFragment(), "Features")
        childFragmentManager.setFragmentResult(
            TAG_SHOP,
            bundleOf(KEY_FOR_TAG_SHOP to list),
        )
        viewBinding.viewPager.adapter = adapter
        viewBinding.viewPager.currentItem = 0
        TabLayoutMediator(viewBinding.tabLayout, viewBinding.viewPager) { tab, position ->
            tab.text = adapter.getTabTitle(position)
        }.attach()
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

    private enum class Direction { LEFT, RIGHT }

    private fun moveNextOrPrevious(delta: Int) {
        if (delta > 0) setupAnimations(Direction.RIGHT)
        if (delta < 0) setupAnimations(Direction.LEFT)
        viewModel?.onSwipe(delta)
    }

    private fun setupAnimations(direction: Direction) {
        // Swipe animations
        viewBinding.isHotSales.inAnimation = when (direction) {
            Direction.RIGHT -> slideInRight
            Direction.LEFT -> slideInLeft
        }
        viewBinding.isHotSales.outAnimation = when (direction) {
            Direction.RIGHT -> slideOutLeft
            Direction.LEFT -> slideOutRight
        }
        // overscroll (no more photos) effect on the side on the screen
        if (viewModel?.stateFlow?.value?.detailsInfo != null) {
            if (viewModel.stateFlow.value.detailsInfo!!.images.size <= 1) {
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

    private fun showRating(rating: Float) {
        for (i in 1..5) {
            if (rating.roundToInt() >= i) {
                when (i) {
                    1 -> viewBinding.ivStar1.isVisible = true
                    2 -> viewBinding.ivStar2.isVisible = true
                    3 -> viewBinding.ivStar3.isVisible = true
                    4 -> viewBinding.ivStar4.isVisible = true
                    5 -> viewBinding.ivStar5.isVisible = true
                }
            } else {
                when (i) {
                    1 -> viewBinding.ivStar1.isVisible = false
                    2 -> viewBinding.ivStar2.isVisible = false
                    3 -> viewBinding.ivStar3.isVisible = false
                    4 -> viewBinding.ivStar4.isVisible = false
                    5 -> viewBinding.ivStar5.isVisible = false
                }
            }
        }
    }

    private fun writeError(view: View, error: String) {
        val snackBarView =
            Snackbar.make(view, error, Snackbar.LENGTH_INDEFINITE)
                .setTextColor(ContextCompat.getColor(requireContext(), R.color.error_text))
                .setAction("Retry") {
                    viewModel?.onRetryClick()
                }
        val sbView = snackBarView.view
        val params = sbView.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        sbView.layoutParams = params
        snackBarView.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
        snackBarView.show()
    }

    companion object {
        const val TAG_SHOP = "tag_shop"
        const val KEY_FOR_TAG_SHOP = "key_for_tag_shop"
    }
}
