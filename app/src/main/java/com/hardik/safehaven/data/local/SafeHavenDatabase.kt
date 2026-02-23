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
        private var INSTANCE : SafeHavenDatabase? = null // we are creating single shared variable for the whole app, not to individual objects
        // INSTANCE = null ?? means -> database not created yet
        // once created it stores the database reference and from then on everyone uses the same one.

        // @Volatile ??
        // Imagine 2 threads:
        //  Thread A checks if INSTANCE is null
        //  Thread B checks if INSTANCE is null
        // Both see null → both create database → crash.
        // @Volatile ensures:
        //  All threads see the latest updated value.
        // It prevents memory inconsistency issues.

        fun getDatabase(context: Context): SafeHavenDatabase { // this is a FACTORY method
            // meaning -> If database already exists, return it
            // if not -> create it safely
            return INSTANCE ?: synchronized(this) {
                // why synchronized() block ??
                //  If two threads try to create database at same time:
                //  Only one thread is allowed inside this block.
                //  Prevents duplicate database creation

                val cryptoManager = CryptoManager() // needed for encryption

                val passphraseManager = DatabasePassphraseManager(context, cryptoManager) // handles DB passphrase securely

                val passphrase = passphraseManager.getOrCreatePassphrase() // this gives SQLCipher its password, without this DB cannot open.

                val factory = SupportFactory(passphrase) // this tells ROOM to use encrypted SQLCipher instead of normal SQLite.

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SafeHavenDatabase::class.java,
                    "safehaven_secure_db"
                )
                    .openHelperFactory(factory)
                    .fallbackToDestructiveMigration()
                    .build()
                // this creates encrypted DB

                INSTANCE = instance // now DB is saved ... the next time someone calls getDatabase(), it won't recreate
                instance
            }
        }

    } // companion object is the static area of the class, it belongs to class, not individual objects.

} // this is the main control room of our database.
// only ONE instance, should exists in the entire app.
// why ??
//  Because multiple database instance waste memory,
//  cause crashes, cause data inconsistency, open multiple connections
//  so we need to ensure only ONE db instance is there ... that is why we are using COMPANION OBJECT and INSTANCE
