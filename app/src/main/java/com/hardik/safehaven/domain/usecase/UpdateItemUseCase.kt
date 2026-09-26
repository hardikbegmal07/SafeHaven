package com.hardik.safehaven.domain.usecase

import com.hardik.safehaven.domain.model.SecureItem
import com.hardik.safehaven.domain.repository.SecureItemRepository

class UpdateItemUseCase(
    private val repository: SecureItemRepository
) {

    suspend operator fun invoke(item: SecureItem) {
        repository.updateItem(item)
    }

}