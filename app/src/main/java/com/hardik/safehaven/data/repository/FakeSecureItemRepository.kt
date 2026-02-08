package com.hardik.safehaven.data.repository

import com.hardik.safehaven.domain.model.SecureItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeSecureItemRepository : SecureItemRepositoryForStaticData {

    private val items = MutableStateFlow<List<SecureItem>>(emptyList())

    override fun getItems(): Flow<List<SecureItem>> = items

    override fun getItemById(id: String): SecureItem? {
        // return items.find { it.id == id }
        return items.value.find { it.id == id }
    }

    override fun addItem(item: SecureItem) {
        // items.add(item)
        items.value += item
    }

    override fun deleteItem(id: String) {
        //items.removeAll { it.id == id }
        items.value = items.value.filterNot { it.id == id }
    }
}
// temporary implementation

// it exists so that we can
//  Build UI
//  Test flows
//  Understand Architecture
// Avoid database complexity early