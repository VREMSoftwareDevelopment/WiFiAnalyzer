# Security & APK verification

This file documents the public signing-certificate fingerprint for APKs published on the project's GitHub releases.

## GitHub release signing certificate (SHA-256)

```
17:EA:63:A0:60:1C:E9:E2:46:F4:25:58:0D:81:2E:7F:5D:41:5B:76:85:D4:97:10:3B:B3:82:AF:52:D3:5D:59
```

## Verify a downloaded APK

Preferred (apksigner - Android build-tools):

```bash
$ANDROID_SDK_ROOT/build-tools/<version>/apksigner verify --print-certs WiFiAnalyzer-<version>.apk
```

Compare the `SHA-256 digest` output to the fingerprint above.

Fallback (v1/JAR-signed APKs only):

```bash
unzip -p WiFiAnalyzer-<version>.apk META-INF/*.RSA > CERT.RSA
openssl pkcs7 -inform DER -in CERT.RSA -print_certs -out cert.pem
openssl x509 -noout -fingerprint -sha256 -in cert.pem
```

## Note

Applies only to APKs downloaded from the GitHub releases page: https://github.com/VREMSoftwareDevelopment/WiFiAnalyzer/releases. Signing may differ for other distribution channels (e.g., Google Play).
