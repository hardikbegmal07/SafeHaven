package com.hardik.safehaven.data.local

import android.content.Context
import androidx.room.Room

object DatabaseProvider { // SINGLETON, object = only ONE instance in the entire app
    // Perfect for shared resources like databases

    private var INSTANCE: SafeHavenDatabase? = null // need a place to
    // store the database after creating it
    // Reuse it next time instead of rebuilding

    fun getDatabase(context: Context): SafeHavenDatabase {
        return INSTANCE ?: synchronized(this) {
            // why synchronized(this) ??
            // Because:
            //  Android apps are multi-threaded, Two threads could call getDatabase() at the same time
            //  synchronized guarantees only one database is created
            val instance = Room.databaseBuilder(
                context.applicationContext, // Database should live as long as the app, applicationContext is safe and leak-free
                SafeHavenDatabase::class.java,
                "safehaven_db" // This is:
                        // The actual SQLite file name, stored internally on the device
            ).build()
            INSTANCE = instance
            instance
        }
    }
} // To ensure a single, thread-safe Room database instance across the app
// and avoid unnecessary database recreation or context leaks