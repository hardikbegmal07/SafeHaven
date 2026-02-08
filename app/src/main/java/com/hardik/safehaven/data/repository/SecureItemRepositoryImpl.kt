package com.hardik.safehaven.data.repository

import com.hardik.safehaven.data.local.dao.SecureItemDao
import com.hardik.safehaven.data.mapper.toDomain
import com.hardik.safehaven.data.mapper.toEntity
import com.hardik.safehaven.domain.model.SecureItem
import com.hardik.safehaven.domain.repository.SecureItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

//  Database (Entity)
//       ↓ (Mapper)
//   Repository
//       ↓ (Domain Model)
//    ViewModel
//       ↓
//       UI

class SecureItemRepositoryImpl(
    private val dao: SecureItemDao
) : SecureItemRepository { // hides ROOM/SQLite details and exposes clean app-friendly data to the rest of our app
    // it is making a promise to provide data in this format (SecureItemRepository), but how i get it is my business.
    // why do we pass SecureItemDoa here ?
    //  DAO knows how to talk to the database
    //  Repository decides how data should look for the app

    override suspend fun addItem(item: SecureItem) {
        dao.insertItem(item.toEntity())
    }

    override suspend fun deleteItem(id: String) {
        dao.deleteItem(id)
    }

    override fun getItems(): Flow<List<SecureItem>> {
        return dao.getAllItems()
            .map { entities ->
                entities.map { it.toDomain() }
            }
    }
}
// What problem does this solve??

//  ❌ Without repository
//  ViewModel talks to DAO
//  UI sees Entity
//  Room leaks everywhere
//  Hard to test
//  Impossible to swap DB with API

//  ✅ With repository
//  ViewModel only knows SecureItem
//  DB is hidden
//  Migrations don’t break UI
//  Backend sync becomes easy