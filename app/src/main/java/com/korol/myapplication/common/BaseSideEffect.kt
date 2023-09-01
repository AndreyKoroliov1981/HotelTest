package com.korol.myapplication.common

interface BaseSideEffect

data class IsNotHomeData(var errorMessage: String) : BaseSideEffect

data class IsNotDetailsData(var errorMessage: String) : BaseSideEffect

data class IsNotCartData(var errorMessage: String) : BaseSideEffect

