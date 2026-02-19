# SafeHaven – Security Architecture

## Overview

SafeHaven is designed as a security-first Android application.

The goal is to protect sensitive user data using a multi-layered encryption strategy based on:

- Android Keystore
- AES-GCM field-level encryption
- SQLCipher database-level encryption
- Clean Architecture separation of concerns

This document defines the complete security architecture of the application.

---

# 1. Security Principles Followed

SafeHaven follows these core security principles:

• Confidentiality  
• Integrity  
• Defense in Depth  
• Least Privilege  
• Zero Trust for Local Storage

---

# 2. Threat Model

We assume the following possible attack scenarios:

1. Attacker gains physical access to device.
2. Attacker extracts database file from device.
3. Attacker tries to open DB in SQLite browser.
4. Attacker modifies encrypted data.
5. Attacker dumps application storage using root.

We DO NOT assume:
- Kernel-level compromise
- Hardware-level exploit
- Android OS vulnerability

---

# 3. Encryption Layers (Defense in Depth)

SafeHaven uses TWO layers of encryption.

---

## Layer 1 – Database-Level Encryption (SQLCipher)

### What It Protects
Entire SQLite database file.

### How It Works
SQLCipher encrypts the database file using AES-256.

Without the correct passphrase:
- The DB file appears as unreadable binary.
- SQLite tools cannot open it.
- Raw file inspection is useless.

### Why We Use It
Protects data at rest if:
- Device is rooted
- DB file is copied
- App storage is dumped

This ensures full database encryption.

---

## Layer 2 – Field-Level Encryption (AES-GCM)

### What It Protects
Specific sensitive fields:

- title
- description
- secret

### How It Works
We use AES-GCM (Authenticated Encryption). (Advance Encryption Standards in Galois/Counter Mode)

AES-GCM provides:
- Confidentiality (encryption)
- Integrity (tamper detection)

If ciphertext is modified:
Decryption throws:
javax.crypto.AEADBadTagException

Meaning:
Tampered data is automatically rejected.

### Why We Use It
Even if:
- Someone bypasses SQLCipher
- DB is partially exposed
- Memory dump occurs

Sensitive fields remain individually encrypted.

---

# 4. Key Management Strategy

## Android Keystore

All AES keys are generated and stored inside Android Keystore.

Properties:
- Hardware-backed (if available)
- Non-exportable
- Cannot be extracted from device

Key properties:
- Algorithm: AES
- Mode: GCM
- Padding: NoPadding
- Key size: 256-bit

Keys never leave Keystore.

---

# 5. Database Passphrase Lifecycle

The SQLCipher database requires a passphrase.

We DO NOT hardcode it.

Instead:

1. Generate random 256-bit passphrase.
2. Encrypt passphrase using AES key from Keystore.
3. Store encrypted passphrase in SharedPreferences.
4. On app startup:
    - Decrypt passphrase
    - Provide to SQLCipher SupportFactory.

This ensures:
- Passphrase is never stored in plaintext.
- Passphrase is protected by hardware-backed Keystore.

---

# 6. Clean Architecture Separation

Encryption logic belongs ONLY to the Data Layer.

ViewModel:
- Does NOT know encryption exists.
- Works with clean domain models.

Repository:
- Encrypts before saving to DB.
- Decrypts before returning data.

Database:
- Encrypted at rest via SQLCipher.

This enforces separation of concerns.

---

# 7. Integrity Protection

AES-GCM provides authentication.

If attacker modifies encrypted data:

Decryption fails.

This ensures:
- No silent data corruption.
- No tampered secrets accepted.

---

# 8. Security Boundaries

Protected:
- Data at rest
- Extracted DB file
- Offline attacks
- File-based inspection

Not Protected:
- Compromised OS
- Screen recording
- Keylogging malware
- Live memory attacks

---

# 9. Security Strength Summary

| Layer                 | Protection Type                   |
|-----------------------|-----------------------------------|
| Android Keystore      | Secure key storage                |
| AES-GCM               | Field confidentiality + integrity |
| SQLCipher             | Database-level encryption         |
| Passphrase encryption | Secure DB unlock                  |

This layered approach significantly increases attack difficulty.

---

# 10. Why This Is Enterprise-Level

Most Android apps:
- Store plain Room DB
- Hardcode encryption keys
- Do not separate encryption layers

SafeHaven:
- Uses hardware-backed key storage
- Uses authenticated encryption
- Encrypts DB and fields
- Implements secure passphrase lifecycle
- Documents attack assumptions

This aligns with security-first mobile application design.

---

# Conclusion

SafeHaven implements a multi-layered local data protection strategy.

By combining:

- Android Keystore
- AES-GCM
- SQLCipher
- Clean Architecture

The app ensures strong protection of sensitive user data against local attacks.