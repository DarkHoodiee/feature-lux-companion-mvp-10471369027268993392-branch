# User Acceptance Checklist — LUX Hoodie

Use this checklist to evaluate the current LUX Hoodie baseline against the Project LUX design and engineering standards.

## 1. Visual Quality (Artistic Review)
*   [ ] **Canonical Neutral Eye**: Silhouette is asymmetrical with an inner taper and outer expansion.
*   [ ] **No "failed geometries"**: The eyes do NOT look like symmetric capsules, ovals, or rounded rectangles.
*   [ ] **Glow Model**: The glow is multi-layered (Core, Bloom, Halo) rather than a single simple blur.
*   [ ] **Raster Layer**: Subtle horizontal scanlines are visible across the visor.
*   [ ] **Hardware-First**: The startup sequence and raster effects feel like electronic hardware rather than character animation.
*   [ ] **Identity Preservation**: Character is recognizable across all expressions.

## 2. Behavioral Quality (Psychological Review)
*   [ ] **Illusion of Life**: The character never appears perfectly frozen.
*   [ ] **Thought before Motion**: Gaze shifts feel motivated by curiosity rather than random timers.
*   [ ] **Responsiveness**: Reaction to touch is immediate but smooth (overshooting/settling).
*   [ ] **Satiation**: LUX correctly demonstrates "boredom" or reduced interest when tapped repeatedly.
*   [ ] **Emotional Readability**: Expressions (Curious, Focused, Happy, Sleepy) are distinct and readable.

## 3. Performance & Stability (Technical Review)
*   [ ] **FPS (60/120Hz)**: Motion is fluid on high-refresh-rate displays.
*   [ ] **Startup Time**: Cold launch to "Dot" phase is < 1 second.
*   [ ] **Memory Consumption**: App maintains a stable memory footprint during long idle sessions.
*   [ ] **Build Success**: Project builds on a clean environment via \`./gradlew assembleDebug\`.

## 4. Final Assessment
*   **Verdict**: [ Approved / Approved with Remarks / Rejected ]
*   **Key Feedback**:
