package com.korol.myapplication.ui.details

import com.korol.domain.details.model.ProductDetails

data class DetailsState(
    val currentImage: Int = 0,
    val currentColor: Int = 0,
    val currentMemorySize: Int = 0,
    val detailsInfo: ProductDetails? = null,
    val initPager: Boolean = false,
    val countProductsInCart: Int = 0
)