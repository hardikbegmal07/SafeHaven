package com.hardik.safehaven.domain.usecase

import com.hardik.safehaven.domain.repository.SecureItemRepository

class DeleteItemUseCase(
    private val repository: SecureItemRepository
) {
    suspend operator fun invoke(id: String) {
        repository.deleteItem(id)
    }
}