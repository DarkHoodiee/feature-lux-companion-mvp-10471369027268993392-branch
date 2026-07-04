# Repository Roadmap — LUX Hoodie

## Phase 1: Build & Safety (Immediate)
*   **Establish Gradle Wrapper**: Initialize \`gradlew\` to prevent environment drift.
*   **Project License**: Add the official project LICENSE file.
*   **Documentation Core**: Create the \`docs/\` directory and populate the \`Architecture_Guide.md\`.
*   **Changelog**: Initialize \`CHANGELOG.md\` with v1.0 through v3.1 history.

## Phase 2: Production Readiness (Short-Term)
*   **Asset Initialization**: Import high-quality launcher icons and define color palettes in \`res/values/colors.xml\`.
*   **Proguard/R8**: Add \`proguard-rules.pro\` and enable shrinking in \`app/build.gradle.kts\`.
*   **Release Configuration**: Define a basic \`signingConfig\` (placeholder) for future automated releases.

## Phase 3: Quality Assurance (Mid-Term)
*   **Core Testing**: Implement unit tests for \`EyePathBuilder\` and \`BehaviorPlanner\`.
*   **CI Pipeline**: Set up a basic GitHub Actions workflow to run \`./gradlew assembleDebug test\` on every PR.
*   **Linter**: Configure \`ktlint\` to enforce code style consistency.

## Phase 4: Developer Experience (Future)
*   **Debug Tooling**: Implement an in-app FPS and State Monitor overlay.
*   **Persistence Layer**: Introduce a Repository pattern for saving user preferences and character traits.
*   **Benchmark Suite**: Create automated performance benchmarks for the renderer.
