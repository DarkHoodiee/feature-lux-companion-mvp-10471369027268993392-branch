# Demo Scenarios — LUX Hoodie

This guide details how to demonstrate the core capabilities of the LUX Hoodie v3.1 baseline.

---

## 1. Startup Sequence (Hardware Boot)
*   **Action**: Launch the app from the home screen.
*   **What happens**: The screen is initially black. A single luminous dot appears at the center, expands horizontally, sweeps upward, then a second line sweeps from the bottom. Finally, the eyes materialize.
*   **Why**: Simulates display hardware powering on rather than a character "opening eyes."
*   **Systems**: \`AutonomousEngine\` (Startup Phase logic), \`LuxFaceRenderer\` (Hardware drawing layers).

## 2. Idle Presence (Breathing & Floating)
*   **Action**: Place the device on a flat surface and observe.
*   **What happens**: The entire face subtly floats with a gentle rotation-Z oscillation. The eyes also exhibit a rhythmic vertical "breathing" motion.
*   **Why**: Creates the "Illusion of Life" by ensuring the character is never perfectly still.
*   **Systems**: \`FaceAnimator\` (Spring physics), \`AutonomousEngine\` (Breathing loop).

## 3. Curiosity (Active Observation)
*   **Action**: Wait for approximately 5–10 seconds without interaction.
*   **What happens**: LUX will periodically shift her gaze to a random point. Occasionally, the eye geometry will morph to the \`CURIOUS\` preset (taller, softer curves) during the shift.
*   **Why**: Demonstrates internal motivation (Curiosity) and environmental scanning.
*   **Systems**: \`BehaviorPlanner\` (Motivation eval), \`BehaviorExecutor\` (Target sequencing).

## 4. Focus (Fixation)
*   **Action**: Tap the screen once.
*   **What happens**: LUX immediately looks toward the tap coordinate. The eyes morph to the \`FOCUSED\` preset (narrower, flattened upper contour).
*   **Why**: Simulates a high-salience attention trigger.
*   **Systems**: \`AttentionSystem\` (Focus level bump), \`FaceController\` (Intent -> Geometry).

## 5. Hardware Refresh
*   **Action**: Observe for 30–60 seconds.
*   **What happens**: A horizontal scanline rapidly sweeps the screen, and the eyes momentarily disappear/flicker.
*   **Why**: Simulates an electronic display "refresh" artifact, distinct from biological blinking.
*   **Systems**: \`BehaviorExecutor\` (Refresh sequence), \`LuxFaceRenderer\` (Clipping mask).

## 6. Satiation (Memory Test)
*   **Action**: Tap the screen repeatedly in the same location (5+ times).
*   **What happens**: The first few taps produce a sharp "LookAt" reaction. Subsequent taps produce a significantly reduced reaction, and LUX may eventually stop looking at the point entirely.
*   **Why**: Demonstrates the "Satiation" logic where repetitive stimuli lose salience.
*   **Systems**: \`AttentionSystem\` (Event history/Diminishing returns).

## 7. Interactive Reactions
*   **Double Tap**: LUX may morph to a \`HAPPY\` state (curved bottom edge) or perform a rapid pulse.
*   **Long Press**: Triggers a sustained \`INVESTIGATE\` behavior with prolonged fixation on the touch point.
