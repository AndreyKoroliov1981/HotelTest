package com.korol.myapplication.ui.room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.korol.domain.room.RoomInteractor
import com.korol.domain.room.model.Room
import com.korol.myapplication.common.BaseViewModel
import com.korol.myapplication.common.IsErrorData
import com.korol.myapplication.ui.hotel.DEBOUNCE_MILS
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

class RoomViewModel
@AssistedInject constructor(
    private val roomInteractor: RoomInteractor,
    @Assisted private val hotelNameArg: String
) : BaseViewModel<RoomState>(RoomState()) {

    @AssistedFactory
    interface RoomViewModelFactory {
        fun create(hotelName: String): RoomViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun providesFactory(
            assistedFactory: RoomViewModelFactory,
            hotelName: String,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(hotelName) as T
            }
        }
    }

    private var job: Job? = null

    init {
        updateState { copy(hotelName = hotelNameArg) }
        getRooms()
    }

    fun refreshLoad() {
        updateState { copy(rooms = emptyList()) }
        getRooms()
    }

    private fun getRooms() {
        job?.cancel()
        job = launch {
            delay(DEBOUNCE_MILS)
            updateState { copy(dataLoading = true) }

            val responseRooms = roomInteractor.getRooms()

            if (responseRooms.errorText == null) {
                updateState { copy(rooms = responseRooms.data ?: emptyList()) }
            } else {
                if (responseRooms.data != null) {
                    updateState { copy(rooms = responseRooms.data ?: emptyList()) }
                }
                sideEffectSharedFlow.emit(IsErrorData(responseRooms.errorText!!))
            }

            updateState { copy(dataLoading = false) }
        }
    }

    fun onClickSendRequest() {
        getRooms()
    }

    fun onClickedChoiceRoom(room: Room) {
        // TODO add click
    }
    fun onSwipeImage(room: Room, delta: Int) {
        // TODO add swipe
    }
}