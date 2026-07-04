# Cognitive_State_Diagram.md

## 1. High-Level Internal State Transitions
This diagram describes the internal "mental" states of LUX and how they transition based on cognitive evaluation.

\`\`\`mermaid
stateDiagram-v2
    [*] --> DORMANT : App Start
    DORMANT --> INITIALIZING : Hardware Trigger
    INITIALIZING --> OBSERVING : Startup Complete

    state OBSERVING {
        [*] --> NEUTRAL_OBSERVATION
        NEUTRAL_OBSERVATION --> CURIOSITY_PEAK : Salient Stimulus
        CURIOSITY_PEAK --> FIXATION : Interest Confirmed
        FIXATION --> NEUTRAL_OBSERVATION : Interest Released / Satiated
        NEUTRAL_OBSERVATION --> DROWSE : Long Inactivity / Evening
    }

    state INTERACTING {
        [*] --> REACTING
        REACTING --> ENGAGED : Interaction Continued
        ENGAGED --> ANNOYED : Over-stimulation
        ANNOYED --> NEUTRAL_OBSERVATION : Cooldown
        ENGAGED --> NEUTRAL_OBSERVATION : Interaction Ceased
    }

    OBSERVING --> INTERACTING : User Touch / Gesture
    INTERACTING --> OBSERVING : Release Attention
\`\`\`

---

## 2. State Descriptions

### DORMANT
The display is off; internal timers (Sensory Memory) are inactive, but Circadian Context continues to track time.

### INITIALIZING
Hardware-only phase. Character logic is blocked until the 7-phase startup sequence completes.

### NEUTRAL_OBSERVATION
The baseline "Observing" state. Low-level curiosity drives subtle look-at shifts.

### CURIOSITY_PEAK
A sudden increase in focus level caused by a new Perception Event. LUX "notices" something.

### FIXATION
Sustained focus. Motion slows down, breathing rhythm regularizes, and the attention span widens to its maximum.

### REACTING
Immediate, non-cognitive response to a gesture (e.g., looking toward a tap).

### ENGAGED
LUX is actively sharing space with the user. Interaction Satiation is active.

### ANNOYED
A defensive state triggered by repeated, high-frequency stimulation (poking). Expression narrows, and reaction speed delays.
