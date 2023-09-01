package com.korol.myapplication.di

import android.content.Context
import com.korol.domain.cart.CartInteractor
import com.korol.domain.home.HomeInteractor
import com.korol.myapplication.ui.cart.CartViewModelFactory
import com.korol.myapplication.ui.home.HomeViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class AppModule(val context: Context) {
    @Provides
    fun provideContext(): Context {
        return context
    }

    @Provides
    fun provideHomeViewModelFactory(homeInteractor: HomeInteractor): HomeViewModelFactory {
        return HomeViewModelFactory(homeInteractor = homeInteractor)
    }

    @Provides
    fun provideCartViewModelFactory(cartInteractor: CartInteractor): CartViewModelFactory {
        return CartViewModelFactory(cartInteractor = cartInteractor)
    }
}