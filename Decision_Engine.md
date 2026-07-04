# Decision_Engine.md

## 1. The Decision Engine (Thinker)
The Decision Engine is the central "CPU" of the cognitive architecture. It executes the "Thought Frame" periodically (every 500ms).

---

## 2. Decision Multi-Factors
To choose an Intention, the engine evaluates four inputs:

1.  **Motivation**: What does LUX *want* to do? (Curiosity, Boredom, etc.)
2.  **Context**: What is LUX *able* to do? (Time of day, current focus level.)
3.  **Memory**: What has LUX *already* done? (Avoid repetition, recent successful behaviors.)
4.  **Attention**: What is LUX *actually* looking at? (Sustain interest vs. release.)

---

## 3. The Weighting Equation
A concrete \`Intention\` is selected by calculating a **Score** for every possible outcome.

\`\`\`
Score(Intention) = (BaseProbability * TraitWeight)
                 + (MotivationBias * ContextMultiplier)
                 - (HabituationPenalty)
                 + (ReinforcementBonus)
\`\`\`

### Example Calculation: \`SCAN_ENVIRONMENT\`
*   **Base Probability**: 0.05
*   **Motivation (Boredom)**: +0.4 (High boredom increases score)
*   **Habituation**: -0.3 (If a scan happened in the last 60s)
*   **Context (Night)**: * 0.5 (Reduced activity at night)
*   **Final Score**: (0.05 + 0.4 - 0.3) * 0.5 = **0.075** (Still low, but higher than baseline).

---

## 4. Conflict Resolution
If multiple Intentions have high scores:

1.  **Goal Preservation**: Favor the Intention that belongs to the current \`Active Goal\`.
2.  **Personality Bias**: If tied, use the Trait Multiplier (e.g., a "Curious" personality favors \`INVESTIGATE\` over \`IDLE_OBSERVE\`).
3.  **Continuity**: Favor smooth transitions (e.g., if already in \`FOCUSED\` geometry, favor Intentions that utilize focus).

---

## 5. Decision Output
The engine returns a \`BehaviorSequence\`.
*   **Chaining**: v4 supports chained intentions (e.g., \`[INVESTIGATE, FIXATE, RELAX]\`) rather than single-step triggers.
*   **Feedback**: The Decision Engine monitors the completion status of the sequence to update Short-Term Memory.
