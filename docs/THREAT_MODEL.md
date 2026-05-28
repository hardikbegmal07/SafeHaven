# SafeHaven Threat Model

## Assets
- Encrypted notes
- User credentials
- Encryption keys
- Session tokens

## Entry Points
- Login screen
- Local database
- Android intents
- Clipboard
- Network APIs

## Attack Vectors
- Reverse engineering
- Rooted devices
- APK tampering
- Debugging
- Memory dumping

## STRIDE Mapping

### Spoofing
- Fake login attempts

### Tampering
- APK modification

### Repudiation
- Unauthorized access denial

### Information Disclosure
- Database extraction

### Denial of Service
- App crash attacks

### Elevation of Privilege
- Root privilege abuse

## Risk Matrix

| Threat | Severity | Mitigation |
|--------|----------|------------|
| Rooted Device | High | Root detection |
| APK Tampering | High | Signature validation |
| Debugging | Medium | Debug detection |
| Reverse Engineering | High | Obfuscation |
