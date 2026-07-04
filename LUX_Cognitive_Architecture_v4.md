# LUX_Cognitive_Architecture_v4.md

## 1. Vision: From Animation to Autonomy
The v3.1 architecture established the physical and behavioral foundation for LUX. v4 moves the project into the "Cognitive Era," where the character is no longer merely reacting to inputs but is actively processing a simulated internal life. The goal is to create the **Illusion of Thought** through persistent state, memory, and evolving motivations.

---

## 2. Structural Evolution from v3.1

| Layer (v3.1) | Role in v4 | Change Description |
| :--- | :--- | :--- |
| **Perception** | **Cognitive Perception** | Expands from raw events to filtered, prioritized semantic stimuli. |
| **Context** | **Working Context** | Transitions from "Current Point" to a multi-dimensional state model including time, goal, and environment. |
| **Thought** | **Cognitive Evaluation** | From simple weights to a dynamic system of competing motivations and memories. |
| **Decision** | **Goal-Oriented Planning** | Replaces static intention selection with a Goal life-cycle system. |
| **Behavior** | **Behavioral Execution** | Remains as the sequencer, but adds chaining, interruption, and resumption capabilities. |
| **Animation** | **Unchanged** | Preserved. The spring physics foundation is now frozen. |
| **Renderer** | **Unchanged** | Preserved. The rendering pipeline is now frozen hardware. |

---

## 3. The Core Cognitive Systems

### 3.1 Perception (Sensing)
Perception is the gateway. In v4, it filters raw sensor data through a "Confidence & Salience" filter.
*   **Inputs**: Touch, Accelerometer, Time, App State, Battery, Light Level.
*   **Filtering**: Noise reduction (e.g., micro-jitters in touch are ignored).
*   **Prioritization**: Sudden events (Touch) always override ambient events (Time/Idle).

### 3.2 Attention (Focus)
Attention in v4 is dynamic and stateful.
*   **Focus**: Current target (Object or Internal Thought).
*   **Fixation**: Sustained focus that increases depth but narrows breadth.
*   **Habituation**: Repetitive stimuli gradually lose salience (LUX "gets used" to it).
*   **Decay**: Focus naturally returns to a neutral baseline without external stimulus.

### 3.3 Context (World Model)
Context provides the "Where and When."
*   **Goal Context**: What was I just doing?
*   **Time Context**: Morning, Afternoon, Evening (Circadian influence).
*   **Recent Interaction**: How has the user been treating me lately?

### 3.4 Memory (Temporal State)
Memory allows LUX to have a past.
*   **Sensory Memory**: Milliseconds (Current frame perception).
*   **Working Memory**: Seconds (Active goal and focus).
*   **Short-Term Memory**: Minutes (Recent events and satiation).
*   **Long-Term Memory**: Days/Weeks (Personality evolution and user habits).

### 3.5 Motivation (Drivers)
Motivations are internal "needs" that compete for attention.
*   **Curiosity**: The need to explore the unknown.
*   **Vigilance**: The need to be alert to the user.
*   **Comfort/Rest**: The need to conserve energy.
*   **Boredom**: The driver to seek new goals after long periods of inactivity.

### 3.6 Goal System (Intentionality)
Goals give LUX a "Why."
*   **Reactive**: Immediate response (e.g., "Look at Touch").
*   **Persistent**: Ambient behaviors (e.g., "Observe Environment").
*   **Active**: The single goal currently driving behavior.

### 3.7 Decision Making (The Engine)
Choices are made by weighing Motivation, Context, and Memory.
*   **Weighting**: (Base Probability * Personality Multiplier) + (Context Bias) - (Habituation Decay).

---

## 4. The Illusion of Personality
Personality in v4 is a set of "Trait Multipliers" that influence all layers.
*   **Curious LUX**: High curiosity motivation, fast attention shifting.
*   **Calm LUX**: High comfort motivation, slow reaction speed, deep fixation.
*   **Playful LUX**: High response to interaction, expressive animation curves.

---

## 5. Daily Rhythm & Environment
LUX's internal clock (Circadian Model) applies biases to motivations:
*   **Morning**: High Vigilance (Ready to start).
*   **Afternoon**: High Curiosity (Exploration phase).
*   **Evening/Night**: High Comfort/Fatigue (Lower energy, more Drowse intentions).

---

## 6. Long-Term Evolution
LUX learns through "Successful Interactions."
*   If a specific expression triggers a positive user reaction (e.g., user stays on screen), that choice is "Reinforced" in the Long-Term Memory.
*   The system remains private: only weights are updated; no raw data is stored.
