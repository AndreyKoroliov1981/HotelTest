package com.korol.myapplication.ui.room

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.korol.myapplication.R
import com.korol.myapplication.app.App

class RoomFragment : Fragment(R.layout.fragment_room) {
    @javax.inject.Inject
    lateinit var vmFactory: RoomViewModelFactory
    private lateinit var viewModel: RoomViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        App.appComponent.injectRoomFragment(this)
        viewModel = ViewModelProvider(this, vmFactory).get(RoomViewModel::class.java)
    }
}