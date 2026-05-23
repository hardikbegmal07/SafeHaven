# 🛡️ SafeHaven

## What is SafeHaven?

**SafeHaven** is a security-first Android application designed to act as a **personal data vault**. The goal of the app is to help users safely store and manage sensitive personal information such as IDs, documents, notes, and credentials.

The app is built with an **offline-first mindset**, meaning user data remains available even without an internet connection. Strong security features such as encryption and biometric authentication are planned and will be introduced incrementally.

SafeHaven is not just an app — it is a learning-driven, production-grade project that demonstrates clean Android architecture, modern development practices, and scalability.

---

## Architecture Overview

SafeHaven follows a **clean, layered MVVM architecture** to ensure separation of concerns, testability, and long-term maintainability.

```
UI (Jetpack Compose)
   ↓
ViewModel (StateFlow)
   ↓
Domain (UseCases)
   ↓
Data (Room / Encrypted Storage)
```

### UI Layer

* Built using **Jetpack Compose**
* Displays state and reacts to changes
* Contains no business logic
* Observes state exposed by ViewModels

### ViewModel Layer

* Owns UI state
* Handles user intents and business logic
* Exposes immutable state using `StateFlow`
* Lifecycle-aware and survives configuration changes

### Domain Layer (Planned)

* Contains business rules and use cases
* Acts as an abstraction between UI/ViewModel and data sources
* Keeps the app logic independent of frameworks

### Data Layer (Planned)

* Handles data persistence and retrieval
* Will use **Room** for local storage
* Will introduce **encrypted storage** for sensitive data
---

## Why MVVM?

The **MVVM (Model-View-ViewModel)** pattern was chosen to enforce clear responsibility boundaries:

- **Separation of concerns**: UI, business logic, and data layers are cleanly separated
- **Testability**: ViewModels and UseCases can be unit-tested independently
- **Lifecycle safety**: ViewModels survive configuration changes
- **Scalability**: New features (encryption, biometrics, backend sync) can be added without breaking existing code

The UI layer remains a *pure renderer of state*, while business logic lives in the ViewModel and Domain layer.

---

## Tech Stack

- **Kotlin** – Primary programming language
- **Jetpack Compose** – Modern declarative UI framework
- **StateFlow & Flow** – Reactive state management
- **Navigation Compose** – In-app navigation
- **Room Database** – Local persistent storage
- **Material 3** – Modern UI components and theming

---

## Current Status (Month 1)

- Clean layered architecture (UI → ViewModel → Domain → Data)
- Feature-based project structure
- Repository pattern implementation
- Domain layer with UseCases
- Room database integration for local persistence
- Reactive data flow using `Flow` + `StateFlow`
- Proper UI state handling using sealed `UiState`
- Input validation using sealed `ValidationResult`
- Navigation graph setup (multi-screen structure)
- Material 3 theming with dark mode support
- Clean app entry point (`SafeHavenApp`)

The application is now architecturally stable and ready for security-focused enhancements.

---

## What’s Next (Month 2)

Planned improvements and features:

- **Encryption** for sensitive data at rest (AES / Encrypted storage)
- **Android Keystore integration** for secure key management
- **Biometric authentication** (Fingerprint / Face unlock)
- Imporving basic security of the application
- Secure data access flow
- Improved UI polish and user experience
- Unit testing for ViewModel and Domain layer

---

## Purpose of This Project

This project is built to:

- Demonstrate real-world Android architecture
- Serve as a strong portfolio project
- Practice security-focused Android development
- Learn scalable and maintainable app design principles

SafeHaven prioritizes **clarity, correctness, security awareness, and long-term maintainability** over shortcuts.
