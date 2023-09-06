package com.korol.myapplication.di

import android.content.Context
import com.korol.myapplication.ui.hotel.HotelFragment
import com.korol.myapplication.ui.room.RoomFragment
import dagger.BindsInstance
import dagger.Component

@Component(modules = [BindDomainModule::class, BindDataModule::class, RetrofitDataModule::class])
interface AppComponent {
    fun injectHotelFragment(hotelFragment: HotelFragment)

    fun injectRoomFragment(roomFragment: RoomFragment)

    // fun injectDetailsFragment(detailsFragment: DetailsFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
        ): AppComponent
    }
}
