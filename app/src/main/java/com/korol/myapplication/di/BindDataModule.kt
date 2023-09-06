package com.korol.myapplication.di

import com.korol.domain.hotel.HotelRepository
import com.korol.domain.room.RoomRepository
import com.korol.repository.hotel.HotelRepositoryImpl
import com.korol.repository.room.RoomRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface BindDataModule {
    @Binds
    fun bindHotelRepositoryImpl(hotelRepositoryImpl: HotelRepositoryImpl): HotelRepository

    @Binds
    fun bindRoomRepositoryImpl(roomRepositoryImpl: RoomRepositoryImpl): RoomRepository
}
