# Remaining Gaps — LUX Hoodie (Deferred Items)

The following items were intentionally deferred as they are not required for a buildable MVP or the v3.1 baseline.

1. **Production Artwork**: Launcher icons use simple vector placeholders. High-fidelity brand assets should be integrated in a future Phase.
2. **Release Signing**: Real `.jks` keystore and credentials are not included for security reasons; placeholders are used in the Gradle configuration.
3. **Advanced CI/CD**: While a `build_verification.yml` was designed, the actual GitHub Actions integration depends on repository hosting.
4. **Screenshot Testing**: Automated visual regression requires a device-equipped environment (Firebase Test Lab or local emulator) and is deferred to Phase C/D.
5. **Analytics/Crashlytics**: Integration of production monitoring tools is deferred to v1.0.
