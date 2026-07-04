# Migration_Plan_v3.1_to_v4.md

## 1. Evolutionary Strategy
The transition from v3.1 to v4 follows the **"Decompress and Expand"** model. We first separate the existing logic into the new layers without changing behavior, then gradually replace hardcoded logic with the new cognitive models.

---

## 2. Phase 1: Infrastructure Decompression (No Visual Changes)

### 2.1 Extraction of FaceAnimator
*   **Action**: Move the current \`SpringSolver\` ticker logic from \`AutonomousEngine\` to a dedicated \`FaceAnimator\` class.
*   **Benefit**: Decouples physics from thought logic.

### 2.2 Extraction of BehaviorExecutor
*   **Action**: Move the sequence timing and target setting (e.g., \`runScanSequence\`) to \`BehaviorExecutor\`.
*   **Benefit**: Engine becomes a high-level manager.

---

## 3. Phase 2: Perception & Context Layer (Stability Focus)

### 3.1 Standardized Perception
*   **Action**: Implement \`EventBus\` and convert \`LuxInteraction\` and Timer triggers into \`LuxEvent\` objects.
*   **Verification**: Ensure all current interactions (Tap, Idle) still trigger the same behaviors.

### 3.2 Introduction of CognitiveContext
*   **Action**: Create the \`CognitiveContext\` model to track \`AttentionPoint\`, \`TimeOfDay\`, and \`FocusLevel\`.
*   **Implementation**: Initial implementation will just pass these through to the existing \`BehaviorPlanner\`.

---

## 4. Phase 3: The Memory & Motivation Models (Logic Focus)

### 4.1 Memory Implementation
*   **Action**: Implement the \`ShortTermMemory\` list and \`Satiation\` logic.
*   **Result**: Repeated Taps will now produce diminishing focus level increases.

### 4.2 Dynamic Motivation
*   **Action**: Replace the static probability weights in \`BehaviorPlanner\` with the \`WinningMotivation\` equation.
*   **Result**: LUX's behavior begins to shift naturally based on time and focus.

---

## 5. Phase 4: Goal-Driven Decision Making (Intelligence Focus)

### 5.1 Goal Lifecycle
*   **Action**: Implement the \`GoalStack\` to support Interruption and Resumption.
*   **Result**: LUX will now return to her previous task after responding to a user tap.

### 5.2 Trait-Based Personalities
*   **Action**: Externalize the Trait Multipliers into a \`PersonalityProfile\`.
*   **Result**: Multiple "Personalities" can be tested without changing the core engine.

---

## 6. Verification Milestones

1.  **Baseline Handoff**: After Phase 1, ensure \`AutonomousEngine\` behaves exactly as it does today.
2.  **Satiation Test**: Confirm that the 5th tap in a row produces a different reaction than the 1st.
3.  **Resumption Test**: Confirm LUX resumes an "Investigate" sequence after a user interruption.
4.  **Circadian Test**: Force the clock to 11 PM and confirm an increase in "Drowse" intentions.
