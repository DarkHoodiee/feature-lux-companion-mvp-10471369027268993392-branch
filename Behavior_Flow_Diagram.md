# Behavior_Flow_Diagram.md

## 1. Information Processing Pipeline
This diagram tracks how an environmental trigger becomes a rendered pixel in LUX Hoodie v4.

\`\`\`mermaid
graph TD
    %% Layer 1: Perception
    S[Sensors: Touch, Time] -->|Raw Data| P[Perceiver]
    P -->|PerceptionEvent| EB[Event Bus]

    %% Layer 2 & 3: Context & Thought
    EB -->|Broadcast| AS[Attention System]
    EB -->|Broadcast| TM[Thinking Agent]
    CTX[Context Model: Time, Goal] --> TM
    MEM[Memory Model: History] --> TM
    AS -->|Focus Level| TM

    %% Layer 4: Decision
    TM -->|Motivation State| DP[Decision Planner]
    DP -->|Intention| BE[Behavior Executor]

    %% Layer 5: Behavior
    BE -->|Target Sequence| FA[Face Animator]

    %% Layer 6 & 7: Animation & Rendering
    FA -->|Physics Frame| LS[LuxFaceState]
    LS -->|StateFlow| RC[Renderer Compositor]
    RC -->|Draw Command| C[Canvas]
\`\`\`

---

## 2. Key Data Transitions

1.  **Sensing to Perception**: Raw (x, y) coordinates are converted to a \`SuddenTouch\` event with a saliency score.
2.  **Perception to Thought**: The Thinking Agent evaluates the event against Memory (e.g., "I saw this 2 seconds ago, ignoring").
3.  **Thought to Decision**: Internal Motivation (e.g., "Vigilance") is converted into a concrete Intention (e.g., "Investigate point").
4.  **Decision to Behavior**: The Intention is expanded into a sequence of geometric targets (e.g., [LookAt Target, Morph to Curious, Wait 2s, Relax]).
5.  **Behavior to Animation**: Target values are fed into \`SpringSolver\`s for per-frame interpolation.
6.  **Animation to Rendering**: The resulting frame is rendered through the hardware layers (Visor → Raster → Eyes).
