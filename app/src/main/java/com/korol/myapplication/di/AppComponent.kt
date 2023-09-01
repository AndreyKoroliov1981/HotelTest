package com.korol.myapplication.di

import com.korol.myapplication.ui.cart.CartFragment
import com.korol.myapplication.ui.details.DetailsFragment
import com.korol.myapplication.ui.home.HomeFragment
import dagger.Component

@Component(modules = [AppModule::class,DomainModule::class,DataModule::class])
interface AppComponent {
    fun injectHomeFragment(homeFragment: HomeFragment)

    fun injectDetailsFragment(detailsFragment: DetailsFragment)

    fun injectCartFragment(cartFragment: CartFragment)
}