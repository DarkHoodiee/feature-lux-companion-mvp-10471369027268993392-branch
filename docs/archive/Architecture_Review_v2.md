# Architecture Review v2 — LUX Hoodie (v3.1)

## 1. Structural Overview
The project successfully implements a **7-layer cognitive hierarchy** through distinct Gradle modules.

1.  **Perception Layer**: Gesture events filtered via `InteractionHandler`.
2.  **Event Bus**: `EventBus.kt` provides decoupled downward communication.
3.  **Attention Layer**: `AttentionSystem.kt` tracks focus and memory.
4.  **Planner Layer**: `BehaviorPlanner.kt` evaluates motivations.
5.  **Executor Layer**: `BehaviorExecutor.kt` sequences intentions.
6.  **Animation Layer**: `FaceAnimator.kt` drives spring-physics transitions.
7.  **Renderer Layer**: `LuxFaceRenderer.kt` performs stateless topology drawing.

---

## 2. Evaluation Against Engineering Principles

### 2.1 Separation of Concerns
**Status: #Verified**
The separation between **Thought** (`BehaviorPlanner`) and **Execution** (`BehaviorExecutor`) is a significant architectural strength. It allows for future expansion of character "intelligence" without modifying the physics or graphics.

### 2.2 Dependency Direction
**Status: #Verified**
All modules depend on the `:domain` core. The Renderer remains stateless and has zero knowledge of "Emotions" or "Intentions," receiving only physical geometry from the Animator.

### 2.3 Scalability
The modular structure is ready for the **v4 Cognitive Architecture** (Memory and Motivation models). The use of Kotlin Flows for state propagation ensures the UI remains responsive even as background cognition becomes more complex.

---

## 3. Recommended Architectural Refinement
-   **Service Registry**: Replace singletons like `EventBus` with a lightweight DI or service locator to improve testability.
-   **Geometry Repository**: Extract eye presets from static objects into a data-driven repository to allow for "character skinning" or runtime refinement.
