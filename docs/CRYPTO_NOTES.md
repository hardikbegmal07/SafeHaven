# Cryptography Notes – SafeHaven

## Symmetric vs Asymmetric

Symmetric:
- Same key encrypts and decrypts
- Fast
- Used for large data
  Example: AES

Asymmetric:
- Public key encrypts
- Private key decrypts
- Slow
  Example: RSA

SafeHaven uses Symmetric encryption (AES).

---

## Why AES?

- Industry standard
- Hardware acceleration on Android
- Secure when used properly

---

## Why AES-GCM?

AES-CBC:
- Provides confidentiality
- DOES NOT provide integrity
- Vulnerable to padding oracle attacks

AES-GCM:
- Provides confidentiality + integrity
- Includes authentication tag
- Detects tampering

If ciphertext is modified → decryption fails.

---

## What is IV?

Initialization Vector:
- Random 12-byte value
- Unique per encryption
- Prevents identical ciphertext for same plaintext

---

## Why not store key in SharedPreferences?

SharedPreferences:
- Stored on filesystem
- Can be extracted on rooted device
- Can be copied

Keystore:
- Key never leaves secure hardware
- Non-exportable
- Cryptographic operations done securely

---

## What is Key Rotation?

- Periodically generating new encryption keys
- Re-encrypting stored data
- Limits impact of key compromise
