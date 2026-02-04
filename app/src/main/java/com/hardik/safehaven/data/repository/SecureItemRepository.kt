package com.hardik.safehaven.data.repository

import com.hardik.safehaven.domain.model.SecureItem
import kotlinx.coroutines.flow.Flow

interface SecureItemRepository {

    fun getItems(): Flow<List<SecureItem>>
    fun getItemById(id: String): SecureItem?
    fun addItem(item: SecureItem)
    fun deleteItem(id: String)

}
// why interface ??
//  DECOUPLING - our viewModel depends on SecureItemRepository not on FakeSecureItemRepository

// That means
// |   Today    |    Tomorrow       |
// | ---------- | ----------------- |
// | Fake repo  | Room DB           |
// | In-memory  | Encrypted storage |
// | No backend | Cloud sync        |

// UI and viewModel code remains the same