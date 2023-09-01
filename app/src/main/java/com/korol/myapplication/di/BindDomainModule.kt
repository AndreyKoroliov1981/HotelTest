package com.korol.myapplication.di

import com.korol.domain.hotel.HotelInteractor
import com.korol.domain.hotel.HotelInteractorImpl
import dagger.Binds
import dagger.Module

@Module
interface BindDomainModule {
    @Binds
    fun bindHotelInteractorImpl(hotelInteractorImpl: HotelInteractorImpl): HotelInteractor
}
