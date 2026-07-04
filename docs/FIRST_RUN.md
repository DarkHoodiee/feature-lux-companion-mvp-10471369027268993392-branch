# First Launch Guide — LUX Hoodie

This guide provides the instructions required to build and run LUX Hoodie for the first time.

## 1. Prerequisites

### Software
*   **Android Studio**: Ladybug (2024.2.1) or newer recommended.
*   **JDK**: Version 17 or 11 (Java 17 preferred).
*   **Android SDK**: API 34 (Android 14) platform and build tools.
*   **Gradle**: Version 8.8 (included via wrapper).

### Hardware
*   **Android Device or Emulator**:
    *   Minimum SDK: API 26 (Android 8.0).
    *   Recommended: API 33+ for optimal performance.
    *   High Refresh Rate (90/120Hz) display highly recommended for motion evaluation.

---

## 2. Setup & Build

1.  **Clone the Repository**:
    \`\`\`bash
    git clone <repository_url>
    cd lux-hoodie
    \`\`\`
2.  **Open in Android Studio**:
    *   Select **File > Open...** and choose the root directory.
    *   Wait for the initial Gradle sync to complete.
3.  **Gradle Sync**:
    *   If sync fails, ensure your **Project Structure > SDK Location** points to a valid Android SDK and JDK 17.
4.  **Build via CLI (Optional)**:
    \`\`\`bash
    ./gradlew assembleDebug
    \`\`\`

---

## 3. Running the App

1.  **Select Target**: Choose your connected device or emulator from the device dropdown.
2.  **Run**: Press the green **Run** (Play) button or use \`Shift + F10\`.
3.  **Deployment**: The app will install and launch automatically.

---

## 4. Expected First Launch Behavior

When LUX Hoodie launches for the first time:
1.  **Hardware Initialization**: A single blue dot appears, expands into a line, and sweeps the screen.
2.  **Materialization**: The eyes gradually fade in as the startup sequence completes.
3.  **Idle State**: LUX enters her baseline "Neutral Observation" state.
4.  **Autonomous Behavior**: After a few seconds, she will begin subtle look-at shifts, breathing, and occasional blinking.

---

## 5. Known Limitations
*   **Placeholder Icons**: The launcher icon is a simple vector silhouette.
*   **No Persistence**: Session data and focus levels reset upon app restart.
*   **UI Controls**: The interface is an immersive "Face Only" view; there are no menus or settings buttons.
*   **Orientation**: portrait orientation is forced to maintain visual proportions.
