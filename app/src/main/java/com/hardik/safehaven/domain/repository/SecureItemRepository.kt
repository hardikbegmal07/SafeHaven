package com.hardik.safehaven.domain.repository

import com.hardik.safehaven.domain.model.SecureItem
import kotlinx.coroutines.flow.Flow

interface SecureItemRepository {

    suspend fun addItem(item: SecureItem)
    suspend fun deleteItem(id: String)
    fun getItems(): Flow<List<SecureItem>>
}