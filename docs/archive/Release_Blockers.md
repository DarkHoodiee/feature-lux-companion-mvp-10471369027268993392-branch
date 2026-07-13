# Release Blockers — LUX Hoodie (v3.1)

The following items are **Critical** and **High** priority tasks that MUST be completed before LUX can be released to testers or production.

---

## 1. Critical (Functionality & Stability)
-   [ ] **Comprehensive Test Harness**: Implement unit tests for `AttentionSystem` (Focus decay/satiation) and `BehaviorExecutor` (Startup/Intention sequencing).
-   [ ] **ProGuard Configuration**: Create `proguard-rules.pro` to prevent R8 from breaking the topology and cognitive models during production minification.

---

## 2. High (Production Hardening)
-   [ ] **Signing Config**: Define `signingConfigs` in `app/build.gradle.kts` to allow for automated production builds.
-   [ ] **Error Boundary**: Implement a top-level `ErrorBoundary` in the Compose UI to handle unexpected cognitive exceptions without crashing the process.
-   [ ] **character Persistence**: Implement a basic storage layer (e.g., DataStore) to persist character "Mood" and "Focus" across application restarts.

---

## 3. High (Visual Polish)
-   [ ] **Renderer Performance Optimization**: Replace the current iterative `drawLine` loop in `LuxFaceRenderer` with a static `Shader` or cached `Bitmap` for scanlines.
-   [ ] **Adaptive Scaling**: Refine `masterScale` calculation to support a wider range of tablet and folding display aspect ratios.
