package com.korol.myapplication.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.korol.domain.home.HomeInteractor

class HomeViewModelFactory(val homeInteractor: HomeInteractor): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(homeInteractor = homeInteractor) as T
    }
}