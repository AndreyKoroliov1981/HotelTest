package com.korol.domain.details

import com.korol.domain.details.model.ProductDetails

class DetailsInteractorImpl(
    private val detailsRepository: DetailsRepository
) : DetailsInteractor {
    override suspend fun getDetails(id: Int): ProductDetails {
        return detailsRepository.getDetails(id)
    }
}