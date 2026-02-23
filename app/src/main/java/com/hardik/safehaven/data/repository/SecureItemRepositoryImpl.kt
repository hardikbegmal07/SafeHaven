package com.hardik.safehaven.data.repository

import com.hardik.safehaven.data.local.dao.SecureItemDao
import com.hardik.safehaven.data.local.entity.SecureItemEntity
import com.hardik.safehaven.data.mapper.toDomain
import com.hardik.safehaven.data.security.CryptoManager
import com.hardik.safehaven.domain.model.SecureItem
import com.hardik.safehaven.domain.repository.SecureItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.UUID

//  Database (Entity)
//       ↓ (Mapper)
//   Repository
//       ↓ (Domain Model)
//    ViewModel
//       ↓
//       UI


// we will be doing Field level encryption in repository
//  ENCRYPTION belongs in DATA layer.
//  VIEWMODEL must NEVER see raw encryption logic.

class SecureItemRepositoryImpl(
    private val dao: SecureItemDao,
    private val cryptoManager: CryptoManager
) : SecureItemRepository { // hides ROOM/SQLite details and exposes clean app-friendly data to the rest of our app
    // it is making a promise to provide data in this format (SecureItemRepository), but how i get it is my business.
    // why do we pass SecureItemDoa here ?
    //  DAO knows how to talk to the database
    //  Repository decides how data should look for the app

    // Encrypted before saving to DB
    override suspend fun addItem(item: SecureItem) {
        withContext(Dispatchers.IO) {
            val encryptedTitle = cryptoManager.encrypt(item.title)
            val encryptedContent = cryptoManager.encrypt(item.content)

            dao.insertItem(
                SecureItemEntity(
                    id = item.id.ifEmpty { UUID.randomUUID().toString() },
                    title = encryptedTitle.cipherText,
                    titleIv = encryptedTitle.iv,
                    content = encryptedContent.cipherText,
                    contentIv = encryptedContent.iv,
                    type = item.type,
                    createdAt = item.createdAt
                )
            )
        }
    }

    // No encryption needed for delete (id is not sensitive)
    override suspend fun deleteItem(id: String) {
        withContext(Dispatchers.IO) {
            dao.deleteItem(id)
        }
    }

    // Decrypt when returning list
    override fun getItems(): Flow<List<SecureItem>> {
        return dao.getAllItems()
            .map { entities ->
                entities.map { it.toDomain(cryptoManager) }
            }
    }

    // Decrypt single item
    override fun getItemById(id: String): Flow<SecureItem?> {
        return dao.getItemById(id)
            .map { it?.toDomain(cryptoManager) }
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