# SafeHaven – Security Model

## 1. Assets (What We Protect)

- User passwords
- Personal notes
- Identification numbers
- Banking details
- Secure metadata

These are highly sensitive and must maintain:
- Confidentiality
- Integrity
- Availability (within reason)

---

## 2. Threat Actors

1. Device Thief
    - Physically steals phone
    - Attempts offline data extraction

2. Malicious App
    - Attempts to access local storage
    - Reads backups

3. Rooted Device User
    - Full filesystem access
    - Memory inspection possible

4. Debuggable Build Exploiter
    - Uses adb backup
    - Dumps databases

---

## 3. Attack Vectors

- Copying Room database file
- Backup extraction (adb backup)
- Memory scraping
- Reverse engineering APK
- Root-level filesystem access
- Tampering with ciphertext

---

## 4. Assumptions

- Device may be compromised
- App sandbox is not sufficient
- Attackers may access raw database
- Keys must NEVER be exportable
- Encryption must provide integrity

---

## 5. Security Strategy

- AES-256-GCM encryption
- Hardware-backed Keystore key
- Non-exportable key
- Unique IV per encryption
- Authentication tag validation
- No key stored in SharedPreferences
- No plaintext stored in Room