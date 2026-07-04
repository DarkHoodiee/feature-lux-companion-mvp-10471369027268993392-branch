# Missing Project Files — LUX Hoodie

## 1. Build & Core Configuration

| File Name | Location | Responsibility | Classification | Implement Now? |
| :--- | :--- | :--- | :--- | :--- |
| **gradlew / gradlew.bat** | Root | Ensures consistent build environment. | **Required to build** | Yes |
| **gradle-wrapper.properties** | \`gradle/wrapper/\` | Specifies Gradle version. | **Required to build** | Yes |
| **ic_launcher.xml** | \`app/src/main/res/drawable/\` | App icon. | **Required for release** | Yes |
| **AndroidManifest.xml (Icons)** | \`app/src/main/\` | Reference icons and labels. | **Required for release** | Yes |
| **proguard-rules.pro** | \`app/\` | Code shrinking and optimization. | **Required for release** | Yes |
| **signingConfig** | \`app/build.gradle.kts\` | Release signing logic. | **Required for release** | Yes |

---

## 2. Documentation & Licensing

| File Name | Location | Responsibility | Classification | Implement Now? |
| :--- | :--- | :--- | :--- | :--- |
| **LICENSE** | Root | Usage and ownership terms. | **Required for release** | Yes |
| **README.md (Expansion)** | Root | Project overview and setup. | **Recommended** | Yes |
| **Architecture_Guide.md** | \`docs/\` | Technical blueprint. | **Recommended** | Yes |
| **Renderer_Handbook.md** | \`docs/\` | Renderer reference. | **Recommended** | Yes |
| **CHANGELOG.md** | Root | History of changes. | **Recommended** | Yes |
| **CONTRIBUTING.md** | Root | Contribution guidelines. | **Optional** | Deferred |
| **SECURITY.md** | Root | Security policy. | **Optional** | Deferred |

---

## 3. Testing & CI/CD

| File Name | Location | Responsibility | Classification | Implement Now? |
| :--- | :--- | :--- | :--- | :--- |
| **EyePathBuilderTest.kt** | \`renderer/src/test/\` | Path math validation. | **Recommended** | Yes |
| **BehaviorPlannerTest.kt** | \`engine/src/test/\` | Thought logic validation. | **Recommended** | Yes |
| **build_verification.yml** | \`.github/workflows/\` | Automated build and test. | **Recommended** | Yes |
| **ScreenshotTests.kt** | \`app/src/androidTest/\` | Visual regression. | **Recommended** | Deferred |
| **detekt.yml** | Root | Static analysis config. | **Optional** | Deferred |

---

## 4. Assets & Tooling

| File Name | Location | Responsibility | Classification | Implement Now? |
| :--- | :--- | :--- | :--- | :--- |
| **colors.xml** | \`app/src/main/res/values/\` | Shared color palette. | **Recommended** | Yes |
| **sounds/** | \`app/src/main/assets/\` | Audio assets. | **Recommended** | Deferred |
| **DebugOverlay.kt** | \`app/src/main/java/.../\` | Performance monitoring. | **Optional** | Deferred |
