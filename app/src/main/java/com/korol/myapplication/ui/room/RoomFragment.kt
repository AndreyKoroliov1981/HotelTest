package com.korol.myapplication.ui.room

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.korol.domain.room.model.Room
import com.korol.myapplication.R
import com.korol.myapplication.app.App
import com.korol.myapplication.common.IsNotHomeData
import com.korol.myapplication.databinding.FragmentRoomBinding
import com.korol.myapplication.ui.room.recycler.RVOnClickButtonChoice
import com.korol.myapplication.ui.room.recycler.RVSwipeLeftImageRoom
import com.korol.myapplication.ui.room.recycler.RVSwipeRightImageRoom
import com.korol.myapplication.ui.room.recycler.RoomRVAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RoomFragment : Fragment(R.layout.fragment_room) {
    private val viewBinding: FragmentRoomBinding by viewBinding()
    private val args: RoomFragmentArgs by navArgs()

    @javax.inject.Inject
    lateinit var vmFactory: RoomViewModel.RoomViewModelFactory
    private val viewModel: RoomViewModel by viewModels {
        RoomViewModel.providesFactory(
            assistedFactory = vmFactory,
            hotelName = args.nameHotel,
        )
    }

    private var roomRVAdapter = RoomRVAdapter(
        object : RVOnClickButtonChoice {
            override fun onClicked(item: Room) {
                viewModel.onClickedChoiceRoom(item)
            }
        },
        object : RVSwipeRightImageRoom {
            override fun onSwipe(item: Room) {
                viewModel.onSwipeImage(item, 1)
            }
        },
        object : RVSwipeLeftImageRoom {
            override fun onSwipe(item: Room) {
                viewModel.onSwipeImage(item, -1)
            }
        },
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        App.appComponent.injectRoomFragment(this)
        viewBinding.rvRoomList.adapter = roomRVAdapter
        setButtonBackListeners()
        setPullToRefresh()

        viewBinding.apply {
            lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    launch {
                        viewModel.stateFlow.collect {
                            if (it.rooms != emptyList<Room>()) {
                                roomRVAdapter.updateList(it.rooms)
                            }
                            viewBinding.pbLoad.isVisible = it.dataLoading
                            viewBinding.tvHotelName.text = it.hotelName
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

    private fun setButtonBackListeners() {
        viewBinding.ivBack.setOnClickListener {
            Navigation.findNavController(viewBinding.root).popBackStack()
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
            Snackbar.make(view, error, Snackbar.LENGTH_INDEFINITE)
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