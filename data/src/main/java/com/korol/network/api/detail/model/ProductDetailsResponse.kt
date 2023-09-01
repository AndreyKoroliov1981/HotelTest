package com.korol.network.api.detail.model

import com.google.gson.annotations.SerializedName

data class ProductDetailsResponse(
    @SerializedName("CPU") val cpu: String,
    val camera: String,
    val capacity: List<String>,
    val color: List<String>,
    val id: String,
    val images: List<String>,
    val isFavorites: Boolean,
    val price: Int,
    val rating: Float,
    val sd: String,
    val ssd: String,
    val title: String,
)