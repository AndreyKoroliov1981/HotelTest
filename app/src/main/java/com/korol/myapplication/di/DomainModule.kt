package com.korol.myapplication.di

import com.korol.domain.cart.CartInteractor
import com.korol.domain.cart.CartInteractorImpl
import com.korol.domain.details.DetailsInteractor
import com.korol.domain.details.DetailsInteractorImpl
import com.korol.domain.home.HomeInteractor
import com.korol.domain.home.HomeInteractorImpl
import com.korol.domain.cart.CartRepository
import com.korol.domain.details.DetailsRepository
import com.korol.domain.home.HomeRepository
import dagger.Module
import dagger.Provides

@Module
class DomainModule {
    @Provides
    fun provideHomeInteractor(homeRepository: HomeRepository): HomeInteractor {
        return HomeInteractorImpl(homeRepository = homeRepository)
    }

    @Provides
    fun provideDetailsInteractor(detailsRepository: DetailsRepository): DetailsInteractor {
        return DetailsInteractorImpl(detailsRepository = detailsRepository)
    }

    @Provides
    fun provideCartInteractor(cartRepository: CartRepository): CartInteractor {
        return CartInteractorImpl(cartRepository = cartRepository)
    }
}