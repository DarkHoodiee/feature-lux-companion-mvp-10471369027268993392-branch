# Motivation_Model.md

## 1. Dynamic Motivation System
Instead of static triggers, v4 uses a system of competing "Biological Needs." Motivations have a \`Level\` and a \`Rate of Change\`.

### 1.1 Curiosity (The Explorer)
*   **Driver**: High Focus + Unknown Environment.
*   **Decay**: Increases slowly over time; decreases rapidly upon successful "Investigation."
*   **Bias**: High in the Afternoon; Low at Night.

### 1.2 Vigilance (The Guardian)
*   **Driver**: Recent Interaction + User Presence.
*   **Decay**: Increases rapidly on Touch; decays naturally toward zero if ignored.
*   **Bias**: Highest in the Morning.

### 1.3 Comfort / Rest (The Conserver)
*   **Driver**: Duration of Activity + Low Battery (future) + Evening Time.
*   **Decay**: Increases during "Drowse" behavior; decreases during high-intensity activity.
*   **Effect**: Reduces expression expressiveness and slows reaction timing.

### 1.4 Boredom (The Stimulus Seeker)
*   **Driver**: Duration of static "Neutral Observation."
*   **Decay**: Resets to zero on any Perception Event; increases exponentially if LUX is idle for > 15s.
*   **Outcome**: Triggers "Scan Environment" or "Look-at Shift" to force new perception.

---

## 2. Motivation Competition
At every "Thought Frame" (approx. 500ms), LUX evaluates her top motivation.

\`\`\`
WinningMotivation = Max(
    (Curiosity * TraitWeight.exploration),
    (Vigilance * TraitWeight.responsiveness),
    (Comfort * TraitWeight.calmness),
    (Boredom * TraitWeight.randomness)
)
\`\`\`

---

## 3. Emotional State Coupling
Motivations drive the internal Emotional State, which is passed to the Behavior layer.

| Primary Motivation | Internal Emotion | Behavioral Profile |
| :--- | :--- | :--- |
| Curiosity | **Inquisitive** | Rapid shifts, tall eyes, soft curves. |
| Vigilance | **Attentive** | Focused eyes, locked target, minimal drift. |
| Comfort | **Relaxed** | Slow motion, lower eye center, frequent blink. |
| Boredom | **Restless** | Erratic scanning, sudden tilts. |
