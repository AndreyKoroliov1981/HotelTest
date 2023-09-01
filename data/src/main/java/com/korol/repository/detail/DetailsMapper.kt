package com.korol.repository.detail

import com.korol.domain.details.model.ProductDetails
import com.korol.network.api.detail.model.ProductDetailsResponse

class DetailsMapper {
    fun mapDetailsFromNetwork(productDetailsResponse: ProductDetailsResponse): ProductDetails =
        ProductDetails(
            cpu = productDetailsResponse.cpu,
            camera = productDetailsResponse.camera,
            capacity = productDetailsResponse.capacity,
            color = productDetailsResponse.color,
            id = productDetailsResponse.id,
            images = productDetailsResponse.images,
            isFavorites = productDetailsResponse.isFavorites,
            price = productDetailsResponse.price,
            rating = productDetailsResponse.rating,
            sd = productDetailsResponse.sd,
            ssd = productDetailsResponse.ssd,
            title = productDetailsResponse.title,
        )
}