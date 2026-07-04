# Memory_Model.md

## 1. The Memory Hierarchy
To create the "Illusion of Life," LUX must demonstrate temporal awareness. v4 introduces a four-stage memory model.

### 1.1 Sensory Memory (Buffer)
*   **Duration**: 100ms - 500ms.
*   **Purpose**: Tracks raw input continuity.
*   **Logic**: Used to filter noise and determine the "Vector" of an interaction (e.g., swipe direction).

### 1.2 Working Memory (Focus)
*   **Duration**: 5s - 15s.
*   **Purpose**: Tracks the active object of interest and the current Goal.
*   **Contents**: Active interest coordinates, focus level intensity, current sequence step.
*   **Logic**: If Working Memory is "Full" (LUX is fixated), new low-salience perceptions are ignored.

### 1.3 Short-Term Memory (Context)
*   **Duration**: 1 min - 30 min.
*   **Purpose**: Tracks recent behavioral events to prevent repetition (Satiation).
*   **Logic**: Every interaction is logged with a "Satiation Weight."
    *   First Tap: 1.0 (Full reaction).
    *   Fifth Tap in 2 mins: 0.2 (Minimal reaction, "Boredom" or "Annoyance" trigger).

### 1.4 Long-Term Memory (Traits)
*   **Duration**: Persistent (Days/Weeks).
*   **Purpose**: Encodes personality evolution and user relationship.
*   **Logic**: Tracks "Engagement Density."
    *   Frequent positive interaction leads to an "Attentive" personality shift.
    *   Long periods of abandonment lead to a "Restrained/Cautious" shift.

---

## 2. Information Retrieval Logic
When a Perception Event occurs, the Thinking Agent queries memory in order:

1.  **Check Short-Term**: "Have I seen this recently?" (Determines intensity).
2.  **Check Working**: "Am I busy with something else?" (Determines interruptibility).
3.  **Check Long-Term**: "How do I usually feel about this?" (Determines emotional coloring).

---

## 3. Forgetting & Reinforcement
*   **Forgetting**: Timestamps are periodically compared against current time. Memory entries are "Evicted" when their duration expires.
*   **Reinforcement**: Successful interactions (e.g., those that sustain attention for > 10s) receive a "Reinforcement Weight," making them more likely to be repeated in similar contexts.
