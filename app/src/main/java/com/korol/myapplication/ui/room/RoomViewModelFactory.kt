package com.korol.myapplication.ui.room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.korol.domain.room.RoomInteractor
import javax.inject.Inject

class RoomViewModelFactory
@Inject constructor(
    private val roomInteractor: RoomInteractor,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RoomViewModel(
            roomInteractor = roomInteractor,
        ) as T
    }
}