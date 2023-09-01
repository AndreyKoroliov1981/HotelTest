package com.korol.myapplication.di

import com.korol.domain.hotel.HotelRepository
import com.korol.repository.hotel.HotelRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface BindDataModule {
    @Binds
    fun bindHotelRepositoryImpl(hotelRepositoryImpl: HotelRepositoryImpl): HotelRepository
}
