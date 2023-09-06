package com.korol.myapplication.ui.room

import com.korol.domain.room.RoomInteractor
import com.korol.myapplication.common.BaseViewModel
import javax.inject.Inject

class RoomViewModel
@Inject constructor(
    private val roomInteractor: RoomInteractor,
) : BaseViewModel<RoomState>(RoomState()) {

}