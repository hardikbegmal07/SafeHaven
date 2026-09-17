# 🔐 SafeHaven

**SafeHaven** is a security-focused Android application designed to act as a **personal data vault** for storing sensitive information such as credentials, notes, personal records, and other private data.

The project is built with an **offline-first approach**, modern Android architecture, and a strong focus on secure local data handling. It is also a learning-driven project where Android engineering, software architecture, and mobile security principles are applied together.

> **SafeHaven is designed to explore how a real-world secure Android application can be structured, protected, and maintained as it grows.**

---

## ✨ Features

### 🔐 Authentication & App Security

- Biometric authentication using Android `BiometricPrompt`
- Support for strong biometrics and device credentials
- Secure lock/unlock flow
- Login and sign-up flow
- Automatic application locking after inactivity
- Session-aware authentication flow
- Exit confirmation when leaving the application
- Screenshot and screen-recording protection using `FLAG_SECURE`
- Rooted-device detection
- Debug-build security warning
- Authentication state managed through `StateFlow`

### 🗄️ Secure Data Vault

- Create and store secure items
- View individual secure items
- Delete secure items
- Item-type based organization
- Offline-first local storage
- Reactive database updates
- Repository abstraction between the application and database

### 🔒 Data Protection

- AES-GCM encryption for sensitive data
- Android Keystore integration for secure key management
- Secure key handling through Android's security APIs
- Encrypted local data storage using SQLCipher
- Encryption/decryption handled outside the UI layer

> Sensitive data is designed to remain local to the device rather than depending on a remote backend.

---

## 🏗️ Architecture

SafeHaven follows a **layered MVVM + Clean Architecture approach** with feature-oriented organization.

```text
┌───────────────────────────────┐
│             UI                │
│       Jetpack Compose         │
└───────────────┬───────────────┘
                ↓
┌───────────────────────────────┐
│          ViewModel            │
│     StateFlow / UI State      │
└───────────────┬───────────────┘
                ↓
┌───────────────────────────────┐
│           Domain              │
│     Use Cases / Business      │
│           Rules               │
└───────────────┬───────────────┘
                ↓
┌───────────────────────────────┐
│            Data               │
│ Repository / Room / SQLCipher │
│      Encryption / Keystore    │
└───────────────────────────────┘
```

### UI Layer

Built with **Jetpack Compose**.

Responsibilities include:

- Rendering application state
- Handling user interactions
- Collecting state from ViewModels
- Navigation
- Displaying loading, success, empty, and error states

The UI does not directly access the database or encryption layer.

### ViewModel Layer

ViewModels act as the bridge between the UI and application logic.

Responsibilities include:

- Managing UI state
- Processing user actions
- Exposing immutable `StateFlow`
- Coordinating use cases
- Managing authentication state
- Surviving configuration changes

Example state flow:

```text
User Interaction
       ↓
    ViewModel
       ↓
   Use Case
       ↓
 Repository
       ↓
   Database
       ↓
      Flow
       ↓
   ViewModel
       ↓
      UI
```

### Domain Layer

The domain layer contains application-specific operations represented through **Use Cases**.

Examples include:

- Add secure item
- Get secure items
- Get secure item by ID
- Delete secure item
- Validate secure item

This keeps business rules independent from Android UI and database implementation details.

### Data Layer

The data layer is responsible for persistence and secure data access.

It includes:

- Repository pattern
- Room database
- SQLCipher-based encrypted database storage
- DAO interfaces
- Encryption/decryption logic
- Android Keystore integration

---

## 🔄 Reactive State Management

SafeHaven uses Kotlin's **Flow** and **StateFlow** for reactive state management.

Database changes can flow through the application without manually refreshing the UI.

```text
Room
 ↓
Flow
 ↓
Repository
 ↓
ViewModel
 ↓
StateFlow
 ↓
Compose UI
```

UI states are represented explicitly rather than relying on scattered Boolean flags.

For example:

```kotlin
sealed class UiState {
    data object Loading : UiState()
    data object Empty : UiState()
    data class Success<T>(val data: T) : UiState()
    data class Error(val message: String) : UiState()
}
```

This makes loading, empty, success, and error states explicit and easier to reason about.

---

## 🧭 Navigation & Authentication Flow

SafeHaven separates authentication state from the main application flow.

```text
                    App Launch
                        │
                        ▼
                 Authentication
                        │
              ┌─────────┴─────────┐
              │                   │
         Authenticated        Login Required
              │                   │
              ▼                   ▼
          Dashboard              Login
              │                   │
              └───────┬───────────┘
                      ▼
                 Secure Vault
```

The application also supports locking the vault after inactivity.

```text
Dashboard
    ↓
Inactivity timeout
    ↓
Locked
    ↓
Biometric / Device Authentication
    ↓
Dashboard
```

Authentication and navigation state are managed independently so that closing the application does not automatically mean logging out.

---

## 🛡️ Security Design

Security is treated as a core architectural concern rather than something added only at the UI level.

### Encryption

Sensitive information is protected using **AES-GCM encryption**.

```text
Plaintext
    ↓
AES-GCM
    ↓
Encrypted Data
    ↓
Database
```

### Key Management

Encryption keys are managed through the **Android Keystore** rather than being hard-coded or stored directly inside the application.

```text
Android Keystore
       ↓
 Encryption Key
       ↓
   AES-GCM
       ↓
 Sensitive Data
```

### Database Protection

SafeHaven uses **SQLCipher** to provide encrypted local database storage.

This provides an additional layer of protection for locally persisted application data.

### Device Security

SafeHaven also includes application-level security measures such as:

- Root detection
- Screenshot/screen-recording prevention
- Biometric/device authentication
- Automatic locking after inactivity
- Debug-build warnings

These mechanisms are intended as defense-in-depth rather than as a guarantee against every possible device compromise.

---

## 🧩 Project Structure

SafeHaven uses a **feature-oriented project structure** while maintaining separation between architectural layers.

A simplified representation:

```text
com.hardik.safehaven
│
├── data
│   ├── database
│   ├── repository
│   └── security
│
├── domain
│   ├── model
│   └── usecase
│
├── presentation
│   ├── auth
│   ├── home
│   ├── additem
│   ├── viewitem
│   └── settings
│
├── navigation
│
└── MainActivity
```

The exact package structure may evolve as the application grows.

---

## 🛠️ Tech Stack

| Technology | Purpose |
|---|---|
| **Kotlin** | Primary programming language |
| **Jetpack Compose** | Declarative UI |
| **Material 3** | UI components and theming |
| **ViewModel** | UI state and lifecycle-aware logic |
| **StateFlow / Flow** | Reactive state management |
| **Navigation Compose** | Application navigation |
| **Room** | Local database abstraction |
| **SQLCipher** | Encrypted database storage |
| **AES-GCM** | Sensitive data encryption |
| **Android Keystore** | Cryptographic key management |
| **BiometricPrompt** | Biometric/device authentication |
| **Coroutines** | Asynchronous programming |
| **Lifecycle** | Lifecycle-aware state collection |

---

## 🧠 Engineering Principles

SafeHaven is built around several principles:

### Separation of Concerns

Each layer has a clearly defined responsibility.

### Dependency Inversion

Higher-level application logic depends on abstractions rather than concrete data implementations.

### Single Source of Truth

Application state is exposed through observable state holders instead of being duplicated across UI components.

### Offline First

Core vault functionality does not depend on an internet connection.

### Security by Design

Encryption, authentication, key management, and device-security considerations are incorporated into the architecture rather than treated as purely UI features.

### Explicit State

Loading, success, empty, error, authentication, and locked states are modeled explicitly.

### Maintainability Over Shortcuts

The project prioritizes understandable architecture and clear boundaries over unnecessarily complex abstractions.

---

## 🚀 Current Capabilities

SafeHaven currently provides the foundation for a secure offline personal vault, including:

- Modern Jetpack Compose UI
- MVVM + layered Clean Architecture
- Feature-oriented project organization
- Repository pattern
- Domain use cases
- Room persistence
- SQLCipher database encryption
- AES-GCM encryption
- Android Keystore key management
- Biometric/device authentication
- Authentication state management
- Automatic inactivity locking
- Secure item creation, viewing, and deletion
- Reactive `Flow` / `StateFlow` data flow
- Explicit UI state handling
- Navigation between authentication and vault screens
- Root-device detection
- Screenshot and screen-recording protection
- Material 3 theming
- Custom SafeHaven UI components

---

<img src="https://github.com/user-attachments/assets/296ec542-252c-4173-8221-78463de5f4e1" width="300" height="auto">
<img src="https://github.com/user-attachments/assets/5f242723-3daa-48ff-93ad-361f43a5336b" width="300" height="auto">
<img src="https://github.com/user-attachments/assets/7b1d6055-0178-4588-a562-d27672328d1f" width="300" height="auto">
<img src="https://github.com/user-attachments/assets/46820d13-5c1e-44ef-abf4-3fb314fd8887" width="300" height="auto">
<img src="https://github.com/user-attachments/assets/39af60c5-bb62-4214-8e3a-a5eb19ea861c" width="300" height="auto">
<img src="https://github.com/user-attachments/assets/d47cbee4-ac68-4182-8efa-b8fbfdf53d3a" width="300" height="auto">

---

## 🔮 Future Improvements

Potential future development areas include:

- Stronger session-management architecture
- More granular inactivity detection
- Improved authentication and credential recovery
- Expanded secure-item types
- Automated security testing
- Comprehensive unit and instrumentation tests
- UI testing
- Security-focused testing and threat modeling
- Backup/export with secure encryption
- Optional secure synchronization
- Additional mobile security hardening

---

## 🎯 Project Goals

SafeHaven is being developed to demonstrate practical experience in:

- Modern Android development
- Kotlin and Jetpack Compose
- MVVM and Clean Architecture
- Reactive application design
- Local data persistence
- Cryptography and key management
- Mobile application security
- Authentication and authorization concepts
- Maintainable software architecture
- Building a real-world application from the ground up

The project is intentionally designed to evolve as new engineering and security concepts are learned and applied.

---

## ⚠️ Security Disclaimer

SafeHaven is a learning and portfolio project and should **not currently be considered a production-grade password manager or security vault**.

Security mechanisms such as encryption, Keystore integration, biometric authentication, SQLCipher, and root detection provide important protections, but secure software requires extensive threat modeling, testing, auditing, secure key lifecycle management, platform-specific hardening, and independent security review.

The project is intended to demonstrate security-aware Android engineering and provide a foundation for continued development.

---

## 👨‍💻 About the Project

**SafeHaven** is being developed as a hands-on exploration of modern Android engineering and mobile security.

The goal is not simply to build another CRUD application, but to understand how **architecture, state management, persistence, authentication, encryption, and mobile security fit together in a real application**.

> **Build securely. Learn continuously. Design for the future.**
