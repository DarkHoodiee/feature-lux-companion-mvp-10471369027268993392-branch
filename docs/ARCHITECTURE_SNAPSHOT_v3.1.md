# Architecture Snapshot — LUX Hoodie (v3.1)

This document provides a technical overview of the current LUX Hoodie implementation.

---

## 1. Module Hierarchy
The project uses a clean multi-module structure to enforce layer isolation:
*   \`:app\`: The Android entry point, UI Composables, and ViewModel.
*   \`:domain\`: Business rules, entities (FaceState), and immutable presets.
*   \`:engine\`: The cognitive heart (Attention, Planning, Execution).
*   \`:renderer\`: The graphics layer (Bézier paths, canvas logic).
*   \`:interaction\`: Translates Android gestures to character events.

---

## 2. The Cognitive Pipeline
Information flows downward through six specialized layers:

1.  **Perception**: Filtered event routing via the \`EventBus\`.
2.  **Context**: \`AttentionSystem\` tracks focus levels, interest points, and interaction history.
3.  **Thought**: \`BehaviorPlanner\` evaluates \`InternalMotivation\` (Curiosity, Rest, Vigilance).
4.  **Decision**: \`Intention\` selection (Investigate, IdleObserve, etc.).
5.  **Behavior**: \`BehaviorExecutor\` sequences targets and durations.
6.  **Animation**: \`FaceAnimator\` manages spring physics tickers.

---

## 3. The Rendering Pipeline
Renderers are stateless and operate in a fixed order:
1.  **Hardware Base**: Visor Depth & Parallax.
2.  **Content**: (Startup Effects) OR (Eye Geometry + Glow).
3.  **Hardware Overlays**: Horizontal Raster lines and Scanning artifacts.

**Optimization**: The \`EyePathBuilder\` uses geometry-based caching to eliminate per-frame allocations.

---

## 4. Key Classes & Data Models

### Domain
*   \`LuxFaceState\`: The immutable single source of truth for a rendered frame.
*   \`EyeGeometry\`: 8-parameter model defining asymmetrical Bézier handles.
*   \`StartupPhase\`: 7-state enum driving the hardware boot sequence.

### Engine
*   \`AutonomousEngine\`: High-level coordinator.
*   \`SpringSolver\`: Physics core providing organic motion.

### Renderer
*   \`LuxFaceCanvas\`: The master Composable that orchestrates drawing.
*   \`EyePathBuilder\`: Translates geometry to Bézier paths.
