package com.hardik.safehaven.domain.usecase

import com.hardik.safehaven.domain.model.SecureItem
import com.hardik.safehaven.domain.repository.SecureItemRepository

// BUSINESS Action
// We use use cases to isolate business logic from UI and data layers, which makes the app easier to test, extend, and secure
class AddItemUseCase(
    private val repository: SecureItemRepository
) { // depends on an abstraction, not ROOM
    suspend operator fun invoke(item: SecureItem) { // operator fun invoke() - it lets us CALL AN OBJECT, LIKE IT'S A FUNCTION
        repository.addItem(item)
    } // This operation may take time (disk, encryption, IO). Don’t block the UI.
}
// Add a secure item to SafeHaven = we are creating one dedicated class for one action


// Without a Use case, our ViewModel would do this :
//  repository.addItem(item)
// which works ... but:
// ❌ ViewModel now knows too much
// ❌ Hard to test business rules
// ❌ Logic spreads everywhere
// ❌ Backend / encryption later becomes messy


// It does NOT know about Room
// It does NOT know about SecureItemDao
// It only knows:
//  “I can add an item somewhere (Room / Encrypted DB / Remote API / Cloud sync / AI classifier)”\


// why NOT put this logic in VIEWMODEL ??
//  Because ViewModel should:
//  ✅ Prepare UI state
//  ❌ NOT define business rules

// ViewModels die and recreate and are UI-scoped and should stay thin ...
// UseCases are reusable, are testable and are business-focused
// "A UseCase represents a single business action, isolated from UI and data details."

