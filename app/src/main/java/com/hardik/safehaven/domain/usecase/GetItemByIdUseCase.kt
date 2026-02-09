package com.hardik.safehaven.domain.usecase

import com.hardik.safehaven.domain.model.SecureItem
import com.hardik.safehaven.domain.repository.SecureItemRepository
import kotlinx.coroutines.flow.Flow

class GetItemByIdUseCase(
    private val repository: SecureItemRepository
) {
    operator fun invoke(id: String): Flow<SecureItem?> =
        repository.getItemById(id)
}
