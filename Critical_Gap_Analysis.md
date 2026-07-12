# Critical Gap Analysis — LUX Hoodie

This report identifies missing capabilities required to move the v3.1 baseline toward a production-ready application.

---

## 1. Critical Gaps (Release Blockers)

### 1.1 Automated Testing
-   **Problem**: Current unit tests only cover `EyeTopology` and `BehaviorSystem` weights.
-   **Requirement**: Need 80%+ coverage for `AttentionSystem` logic and `BehaviorExecutor` state transitions.
-   **Status**: **Missing**.

### 1.2 ProGuard/R8 Hardening
-   **Problem**: No rules exist to protect the cognitive models and renderer logic from obfuscation/decompilation.
-   **Requirement**: Production-ready `proguard-rules.pro`.
-   **Status**: **Missing**.

---

## 2. High Priority Gaps (Pre-Beta)

### 2.1 UI/UX Instrumentation
-   **Gap**: No logging or analytics to track behavioral success or character "engagement."
-   **Requirement**: Event logging interface in the `interaction` module.

### 2.2 Character Persistence
-   **Gap**: If the app is killed, all "Mood" and "Attention" data is lost.
-   **Requirement**: DataStore or Room implementation in `:domain` to save long-term memory.

---

## 3. Medium Priority Gaps (Scalability)

### 3.1 Adaptive Resources
-   **Gap**: Character spacing is currently hardcoded for a 1000px reference width.
-   **Requirement**: Better use of Compose `Density` and screen-size specific resources.

### 3.2 Performance Profiling
-   **Gap**: No automated performance gate to detect frame drops during complex eye morphs.
-   **Requirement**: Macrobenchmark module.
