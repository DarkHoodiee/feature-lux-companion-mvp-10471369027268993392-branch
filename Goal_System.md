# Goal_System.md

## 1. Goal-Driven Intentionality
To move from "Reaction" to "Intention," v4 introduces a hierarchy of Goals. A Goal is a persistent state that defines a desired behavioral outcome.

### 1.1 Goal Types

| Category | Life Span | Priority | Description |
| :--- | :--- | :--- | :--- |
| **Reactive** | 1s - 5s | **High** | Response to external stimuli (e.g., \`LookAt(touch)\`). |
| **Active** | 5s - 15s | **Medium** | Derived from Motivation (e.g., \`InvestigateObject\`). |
| **Persistent** | Indefinite | **Low** | Baseline survival/presence (e.g., \`AmbientPresence\`). |

---

## 2. Goal Life Cycle
Goals are objects with a lifecycle, allowing LUX to be interrupted and resume thoughts.

1.  **Selection**: The Decision Engine picks a Goal based on Motivation and Context.
2.  **Activation**: The Goal is added to Working Memory; its first sequence step begins.
3.  **Interruption**: If a Higher Priority Goal (Reactive) appears, the current Active Goal is "Paused."
4.  **Resumption**: Once the Reactive Goal "Completes" or "Releases," the Paused Goal is re-evaluated for resumption.
5.  **Completion**: The Goal's success criteria are met (e.g., "Observation duration reached").
6.  **Cancellation**: The Goal is abandoned (e.g., because motivation dropped or focus was lost).

---

## 3. Example Sequence: Interrupted Curiosity

1.  **Motivation**: Boredom increases.
2.  **Selection**: Goal: \`Investigate(random_point)\` (Active).
3.  **Action**: Eyes move toward point.
4.  **Event**: User Taps screen (High Salience).
5.  **Interruption**: Goal: \`ReactToTap(coord)\` (Reactive) overrides \`Investigate\`.
6.  **Action**: Eyes quickly shift toward user touch.
7.  **Resumption**: \`ReactToTap\` finishes. LUX remembers she was investigating a point.
8.  **Outcome**: Eyes return to the original investigation point before finally relaxing to Neutral.
