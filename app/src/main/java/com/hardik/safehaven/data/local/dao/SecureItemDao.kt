package com.hardik.safehaven.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hardik.safehaven.data.local.entity.SecureItemEntity
import kotlinx.coroutines.flow.Flow

// DAO - Data Access Object
@Dao
interface SecureItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: SecureItemEntity) // Save the item in the database
    // Room converts SecureItemEntity into SQL
    // It runs an INSERT query
    // If an item with the same id already exists: REPLACE it, this gives update-for-free
    // Why suspend ??
    //  Database access is slow,
    //  It must NOT run on the main thread
    //  suspend forces us to call it from a coroutine

    @Query("SELECT * FROM secure_items ORDER BY createdAt DESC")
    fun getAllItems(): Flow<List<SecureItemEntity>>
    // Give me all items, newest first, and keep updating me when data changes
    // Room checks your SQL while the app is being built, so mistakes fail fast instead of crashing the app later.
    // why Flow<List<SecureItemEntity>> ??
    //  It means:
    //   We don’t fetch data once instead, we subscribe to data changes
    //   Whenever, an item is added or an item is deleted Room automatically:
    //   A) Emits a new list
    //   B) UI updates
    //   C) You write zero refresh code

    @Query("DELETE FROM secure_items WHERE id = :id")
    suspend fun deleteItem(id: String) // Delete the item whose ID matches this value
    // Room safely:
    //  i) Replaces :id with the actual value
    //  ii) Prevents SQL injection
    //  iii) Generates the final SQL
    // suspend because DB work is slow

}
// This file is ROOM's instruction manual
//  Hey Room, These are the only things you're allowed to do with the database.

// IMP - we will never implement this interface myself.
//  Room will do that


