package com.hardik.safehaven.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hardik.safehaven.data.local.dao.SecureItemDao
import com.hardik.safehaven.data.local.entity.SecureItemEntity
import com.hardik.safehaven.data.security.CryptoManager
import com.hardik.safehaven.data.security.DatabasePassphraseManager
import net.sqlcipher.database.SupportFactory

@Database(
    entities = [SecureItemEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SafeHavenDatabase: RoomDatabase() { // RoomDatabase() - is the DATABASE HOLDER -
    // the single place that manages the SQLite database
    abstract fun secureItemDao(): SecureItemDao
    // expose the only allowed way to talk to the database (via DAO), keeping everything safe and structured

    companion object {
        @Volatile
        private var INSTANCE : SafeHavenDatabase? = null
    } // companion object is the static area of the class

    fun getDatabase(context: Context): SafeHavenDatabase {
        return INSTANCE ?: synchronized(this) {

            val cryptoManager = CryptoManager()

            val passphraseManager = DatabasePassphraseManager(context, cryptoManager)

            val passphrase = passphraseManager.getOrCreatePassphrase()

            val factory = SupportFactory(passphrase)

            val instance = Room.databaseBuilder(
                context.applicationContext,
                SafeHavenDatabase::class.java,
                "safehaven_secure_db"
            )
                .openHelperFactory(factory)
                .fallbackToDestructiveMigration()
                .build()

            INSTANCE = instance
            instance
        }
    }
}