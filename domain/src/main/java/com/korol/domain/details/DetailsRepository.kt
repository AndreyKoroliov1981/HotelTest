package com.korol.domain.details

import com.korol.domain.details.model.ProductDetails

interface DetailsRepository {
    suspend fun getDetails(id: Int) : ProductDetails
}