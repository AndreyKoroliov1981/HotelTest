package com.korol.myapplication.ui.room

import com.korol.domain.room.model.Room

data class RoomState(
    val rooms: List<Room> = emptyList(),
    val dataLoading: Boolean = false,
    val currentsPhoto: List<Int> = emptyList(),
)
