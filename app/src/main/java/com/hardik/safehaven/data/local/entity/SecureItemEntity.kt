package com.hardik.safehaven.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// CREATE TABLE secure_items (
//     id TEXT PRIMARY KEY,
//     title TEXT,
//     content TEXT,
//     type TEXT,
//     createdAt INTEGER
// )

@Entity(tableName = "secure_items") // created a table names "secure_items"
data class SecureItemEntity( // DATABASE
    @PrimaryKey val id: String, // unique identifier for each row
    // Why String not Int ??
    // Because:
    //  We want UUIDs
    //  They work offline
    //  They work with future backend sync
    //  They avoid ID collisions
    val title: String,
    val content: String,
    val type: String, // Why not 'ItemType' enum ??
    // Because ROOM is a persistence layer, not a domain layer.
    // Room:
    //  doesn't understand enums semantically
    //  shouldn't depend on business logic
    // So we store the enum as:
    //  type = "NOTE"
    //  type = "PASSWORD"
    // Then we convert it outside Room.
    /////////////////////// This is called model mapping  /////////////////////////////////////
    val createdAt: Long
) // ROOM does not care about the enums - domain does
// this class is NOT our app's main model, It is ONLY the shape of a row in a SQLite table

// why does this class exists ??
//  Bcz ROOM needs its own model, Room cannot (and should not) work directly with
//   UI models
//   Domain models
//   Business logic concepts

