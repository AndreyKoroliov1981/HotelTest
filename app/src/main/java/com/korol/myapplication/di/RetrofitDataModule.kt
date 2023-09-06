package com.korol.myapplication.di

import com.korol.myapplication.BuildConfig
import com.korol.network.api.hotel.HotelRetrofitService
import com.korol.network.api.room.RoomRetrofitService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

@Module
class RetrofitDataModule {

    @Provides
    fun provideHotelRetrofitService(retrofit: Retrofit): HotelRetrofitService {
        return retrofit.create(HotelRetrofitService::class.java)
    }

    @Provides
    fun provideRoomRetrofitService(retrofit: Retrofit): RoomRetrofitService {
        return retrofit.create(RoomRetrofitService::class.java)
    }

    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_ENDPOINT)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
