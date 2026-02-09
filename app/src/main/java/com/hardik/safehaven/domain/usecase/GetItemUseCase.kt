package com.hardik.safehaven.domain.usecase

import com.hardik.safehaven.domain.model.SecureItem
import com.hardik.safehaven.domain.repository.SecureItemRepository
import kotlinx.coroutines.flow.Flow

class GetItemUseCase(
    private val repository: SecureItemRepository
) {
    operator fun invoke(): Flow<List<SecureItem>> {
        return repository.getItems()
    }
}