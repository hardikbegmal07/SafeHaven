# Integrity Testing – AES-GCM

Test:

1. Encrypt string
2. Manually modify ciphertext
3. Attempt decrypt

Result:

javax.crypto.AEADBadTagException

Meaning:
Authentication tag validation failed.

Conclusion:
Data integrity protection is working.

If attacker modifies encrypted data → decryption fails.