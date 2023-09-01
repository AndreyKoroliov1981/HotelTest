package com.korol.myapplication.ui.details

import com.korol.myapplication.R

enum class DetailsPagerEnum(val title: Int, val layoutId: Int) {
    SHOP(R.string.textShop, R.layout.item_shop_details_fragment),
    DETAILS(R.string.textDetails, R.layout.item_details_details_fragment),
    FEATURES(R.string.textFeatures, R.layout.item_features_details_fragment)
}