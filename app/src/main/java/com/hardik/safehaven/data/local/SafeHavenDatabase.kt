package com.hardik.safehaven.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hardik.safehaven.data.local.dao.SecureItemDao
import com.hardik.safehaven.data.local.entity.SecureItemEntity

@Database(
    entities = [SecureItemEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SafeHavenDatabase: RoomDatabase() { // RoomDatabase() - is the DATABASE HOLDER -
    // the single place that manages the SQLite database
    abstract fun secureItemDao(): SecureItemDao // expose the only allowed way to talk to the database (via DAO), keeping everything safe and structured
}