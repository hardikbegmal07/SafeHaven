# SafeHaven OWASP Mobile Security Mapping

## M1 - Improper Credential Usage
Protection:
- Android Keystore
- AES-GCM encryption
- Biometric authentication
- Session timeout

## M2 - Inadequate Supply Chain Security
Protection:
- Dependency verification
- R8 obfuscation
- Signed APK validation

## M5 - Insecure Communication
Protection:
- HTTPS only
- Retrofit secure networking
- Certificate pinning (future)

## M7 - Client Code Quality
Protection:
- Debug detection
- Root detection
- Tamper detection
- Obfuscation
- Runtime security checks