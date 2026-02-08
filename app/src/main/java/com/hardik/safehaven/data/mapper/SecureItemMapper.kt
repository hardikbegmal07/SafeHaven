package com.hardik.safehaven.data.mapper

import com.hardik.safehaven.data.local.entity.SecureItemEntity
import com.hardik.safehaven.domain.model.SecureItem
import java.util.UUID

// MAPPER - converts data from one "shape" to another --
// **** Mappers protect the app from database decisions and protect the database from app decisions ****

//  Why ?? Because different layers of our app, need data in different shapes ...
//  Mapper is the bridge between these two worlds
//  Database(Entity) <-> Mapper <-> App(Domain)

//      Room (SQLite)
//          ↓
//    SecureItemEntity   ← storage shape
//          ↓   (toDomain)
//      SecureItem       ← app meaning
//          ↓
//  UseCases / ViewModels / UI

fun SecureItemEntity.toDomain(): SecureItem {
    return SecureItem(
        id = id,
        title = title,
        content = content,
        type = type,
        createdAt = createdAt
    )
}
// here we are converting a database ROW into an app/business object, we can use
// it is used when we are reading data from Room, before data reaches, use-cases, viewModels, UI
// Because:
//  Room should never leak into UI
//  ViewModels should never see @Entity
//  Business logic should not depend on database annotations
// This function cleans the data before passing it upward.


fun SecureItem.toEntity(): SecureItemEntity {
    return SecureItemEntity(
        id = id.ifEmpty { UUID.randomUUID().toString() }, // This gives me:
        // Create + Update with one function
        // Offline-safe unique IDs
        // Future backend compatibility
        title = title,
        content = content,
        type = type,
        createdAt = createdAt
    )
}
// take an app object and convert it into something the database can store ...
// it is used when saving data to Room, before calling DAO methods

// IMP -> Why not just use SecureItem everywhere ??
// Because tomorrow you might need to "Encrypt fields in DB" OR "Rename DB columns" OR
// "Add metadata only needed for storage" OR Change DB without touching UI

// With mappers:
//  Database changes stay in data layer
//  App logic remains untouched
//  That’s real separation of concerns.


