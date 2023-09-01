package com.korol.myapplication.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.GestureDetector
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.widget.FrameLayout
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
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.korol.myapplication.R
import com.korol.myapplication.app.App
import com.korol.myapplication.common.IsNotHomeData
import com.korol.myapplication.databinding.FragmentHomeBinding
import com.korol.myapplication.ui.cart.recycler.CartRVAdapter
import com.korol.myapplication.ui.cart.recycler.RVOnClickBasketListener
import com.korol.myapplication.ui.cart.recycler.RVOnClickCountListener
import com.korol.myapplication.ui.home.recycler.HomeRVAdapter
import com.korol.myapplication.ui.home.recycler.RVOnClickListener
import com.korol.network.api.cart.model.Gadget
import com.korol.network.api.home.model.BestSeller
import com.korol.network.api.home.model.HomeStore
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.abs

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewBinding: FragmentHomeBinding by viewBinding()

    @javax.inject.Inject
    lateinit var vmFactory: HomeViewModelFactory
    private lateinit var viewModel: HomeViewModel

    // Animations
    private var slideInLeft: Animation? = null
    private var slideOutRight: Animation? = null
    private var slideInRight: Animation? = null
    private var slideOutLeft: Animation? = null
    private var overscrollLeftFadeOut: Animation? = null
    private var overscrollRightFadeOut: Animation? = null

    private lateinit var overscrollLeft: View
    private lateinit var overscrollRight: View

    private var homeRVAdapter = HomeRVAdapter(
        object : RVOnClickListener {
            override fun onClicked(item: BestSeller) {
                val action = HomeFragmentDirections.actionFragmentHomeToFragmentDetails(item.id)
                Navigation.findNavController(viewBinding.root).navigate(action)
            }
        }
    )

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity?.applicationContext as App).appComponent.injectHomeFragment(this)
        viewModel = ViewModelProvider(this, vmFactory).get(HomeViewModel::class.java)
        viewBinding.rvBestSellerList.adapter = homeRVAdapter
        setSelectCategoryListeners()
        setFilterListener()
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
                        viewModel.stateFlow.collect {
                            setSelectCategory(it.selectCategory)
                            clearSelectCategory(it.clearSelectCategory)
                            if (it.hotSalesList.isNotEmpty()) {
                                Glide.with(requireActivity())
                                    .load(it.hotSalesList[it.currentHotSales].picture)
                                    .placeholder(R.drawable.ic_downloading)
                                    .error(R.drawable.ic_error).into(ivHotSales)
                                flIsNew.isVisible = it.hotSalesList[it.currentHotSales].isNew
                                tvTitleHotSales.text = it.hotSalesList[it.currentHotSales].title
                                tvTitleHotSales.isVisible = true
                                tvSubtitleHotSales.text =
                                    it.hotSalesList[it.currentHotSales].subtitle
                                tvSubtitleHotSales.isVisible = true
                                btnHotSalesBuy.isVisible = it.hotSalesList[it.currentHotSales].isBuy
                            } else {
                                flIsNew.isVisible = false
                                tvTitleHotSales.isVisible = false
                                tvSubtitleHotSales.isVisible = false
                                btnHotSalesBuy.isVisible = false
                            }
                            if (it.bestSellerList.isNotEmpty()) {
                                rvBestSellerList.isVisible = true
                                homeRVAdapter.updateList(it.bestSellerList)
                            } else {
                                rvBestSellerList.isVisible = false
                            }
                        }
                    }

                    launch {
                        viewModel.sideEffect.collectLatest {
                            if (it is IsNotHomeData) {
                                writeError(view, it.errorMessage)
                            }
                        }
                    }
                }
            }
        }
    }

    private inner class SwipeListener : GestureDetector.SimpleOnGestureListener() {
        override fun onFling(
            e1: MotionEvent, e2: MotionEvent,
            velocityX: Float, velocityY: Float
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

        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            if (viewModel.stateFlow.value.hotSalesList != emptyList<HomeStore>()) {
                val action = HomeFragmentDirections.actionFragmentHomeToFragmentDetails(
                    viewModel.stateFlow.value.hotSalesList[viewModel.stateFlow.value.currentHotSales].id
                )
                Navigation.findNavController(viewBinding.root).navigate(action)
            }
            return false
        }
    }

    private enum class Direction { LEFT, RIGHT }

    private fun moveNextOrPrevious(delta: Int) {
        if (viewModel.stateFlow.value.hotSalesList != emptyList<HomeStore>()) {
            if (delta > 0) setupAnimations(Direction.RIGHT)
            if (delta < 0) setupAnimations(Direction.LEFT)
            viewModel.onSwipe(delta)
        }
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
        if (viewModel.stateFlow.value.hotSalesList.size <= 1) {
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

    private fun setFilterListener() {
        viewBinding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.btnFilter -> {
                    viewModel.onFilterClick()
                    val action = HomeFragmentDirections.actionFragmentHomeToFragmentFilters()
                    Navigation.findNavController(viewBinding.root).navigate(action)
                    true
                }
                else -> false
            }
        }
    }

    private fun setSelectCategoryListeners() {
        viewBinding.vPhone.setOnClickListener {
            viewModel.onClickSelectCategoryView(0)
        }
        viewBinding.vComputer.setOnClickListener {
            viewModel.onClickSelectCategoryView(1)
        }
        viewBinding.vHealth.setOnClickListener {
            viewModel.onClickSelectCategoryView(2)
        }
        viewBinding.vBooks.setOnClickListener {
            viewModel.onClickSelectCategoryView(3)
        }
        viewBinding.vAuto.setOnClickListener {
            viewModel.onClickSelectCategoryView(4)
        }
    }

    private fun writeError(view: View, error: String) {
        val snackBarView =
            Snackbar.make(view, error, Snackbar.LENGTH_INDEFINITE)
                .setTextColor(ContextCompat.getColor(requireContext(), R.color.error_text))
                .setAction("Retry") {
                    viewModel.onRetryClick()
                }
        val sbView = snackBarView.view
        val params = sbView.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        sbView.layoutParams = params
        snackBarView.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
        snackBarView.show()
    }

    private fun clearSelectCategory(id: Int) {
        when (id) {
            0 -> {
                viewBinding.vPhone.setBackgroundTintList(
                    ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.white
                    )
                )
                viewBinding.ivPhone.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.select_category_background
                    )
                )
            }
            1 -> {
                viewBinding.vComputer.setBackgroundTintList(
                    ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.white
                    )
                )
                viewBinding.ivComputer.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.select_category_background
                    )
                )
            }
            2 -> {
                viewBinding.vHealth.setBackgroundTintList(
                    ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.white
                    )
                )
                viewBinding.ivHealth.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.select_category_background
                    )
                )
            }
            3 -> {
                viewBinding.vBooks.setBackgroundTintList(
                    ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.white
                    )
                )
                viewBinding.ivBooks.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.select_category_background
                    )
                )
            }
            4 -> {
                viewBinding.vAuto.setBackgroundTintList(
                    ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.white
                    )
                )
                viewBinding.ivAuto.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.select_category_background
                    )
                )
            }
        }
    }

    private fun setSelectCategory(id: Int) {
        when (id) {
            0 -> {
                viewBinding.vPhone.setBackgroundTintList(
                    ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.orange
                    )
                )
                viewBinding.ivPhone.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                    )
                )
            }
            1 -> {
                viewBinding.vComputer.setBackgroundTintList(
                    ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.orange
                    )
                )
                viewBinding.ivComputer.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                    )
                )
            }
            2 -> {
                viewBinding.vHealth.setBackgroundTintList(
                    ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.orange
                    )
                )
                viewBinding.ivHealth.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                    )
                )
            }
            3 -> {
                viewBinding.vBooks.setBackgroundTintList(
                    ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.orange
                    )
                )
                viewBinding.ivBooks.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                    )
                )
            }
            4 -> {
                viewBinding.vAuto.setBackgroundTintList(
                    ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.orange
                    )
                )
                viewBinding.ivAuto.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                    )
                )
            }
        }
    }
}