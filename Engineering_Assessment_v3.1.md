# LUX Hoodie: Engineering Assessment (v3.1)

## 1. Architecture Review

### Evaluation
The implementation follows a modular multi-module architecture that successfully isolates concerns between domain logic, engine orchestration, and rendering.

*   **Separation of Concerns**: Excellent. The distinction between `Intention` (Decision) and `Executor` (Behavior) is clear.
*   **Dependency Direction**: Proper. All modules depend on `:domain`, and lower layers (Renderer) have no knowledge of higher layers (Planner).
*   **State Flow**: The project uses a "Single Source of Truth" pattern with `LuxFaceState` via Kotlin Flows.
*   **Scalability**: High. The 7-layer hierarchy (Perception -> Event Bus -> Attention -> Planner -> Executor -> Animator -> Renderer) is well-defined.

### Findings
*   **God Object Tendency**: `AutonomousEngine.kt` currently manages secondary behaviors (breathing, floating, blinking) directly. These should be moved to specialized systems or the `BehaviorExecutor`.
*   **Coupling**: `FaceAnimator` is tightly coupled to the engine's `CoroutineScope` and `MutableStateFlow`.

---

## 2. Rendering Review

### Evaluation
The renderer is stateless and geometry-driven, adhering to the core project principles.

*   **LuxFaceCanvas**: Effectively orchestrates the 3-phase pipeline (Base -> Content -> Overlays).
*   **EyePathBuilder**: Uses geometry-based caching, which significantly reduces per-frame allocations.
*   **StartupRenderer**: Integrated into `LuxFaceRenderer.kt`, correctly implementing the 7-phase sequence.

### Findings
*   **Hardcoded Geometry**: Spacing (160f), look offsets (55f, 45f), and bloom widths (10f, 28f, 55f) are hardcoded in `LuxFaceRenderer.kt`.
*   **Inefficiencies**: `drawRasterLines` uses a `while` loop to draw lines every frame. This could be optimized using a `Shader` or a cached `Bitmap`.
*   **Caching Limitation**: `EyePathBuilder` only caches the single most recent path. If eyes have different geometries (asymmetric expressions), it triggers re-computation every frame.

---

## 3. Visual Fidelity Review

### Evaluation
The implementation achieves a high-quality "Illusion of Life" that surpasses standard Android UI.

*   **Eye Silhouette**: The asymmetrical "leaf" profile with `innerTaper` and `outerExpansion` is correctly implemented in `EyePathBuilder`.
*   **Glow Model**: The 5-layer emissive system (Core -> Base -> Tight Bloom -> Soft Bloom -> Ambient Halo) produces a believable cerulean glow.
*   **Movement**: `SpringSolver` provides organic, non-linear transitions (Accelerate -> Overshoot -> Settle).

### Findings
*   **Raster Effect**: The horizontal lines are static. Adding a slight "scan jitter" or variable transparency would enhance the hardware realism.
*   **Startup Timing**: The phases are hardcoded in `BehaviorExecutor`. Adding parametric control over startup speed would improve polish.

---

## 4. Technical Debt

| Priority | Item | Description |
| :--- | :--- | :--- |
| **Critical** | Hardcoded Constants | Geometry, colors, and timing values are scattered throughout the renderer. |
| **High** | Engine Bloat | Breathing, blinking, and floating logic should be moved out of `AutonomousEngine`. |
| **Medium** | Path Caching | `EyePathBuilder` cache should support at least 2 entries (left/right eye) to avoid thrashing. |
| **Medium** | Raster Performance | Loop-based line drawing in `DrawScope` is inefficient for high-refresh-rate displays. |
| **Low** | Event Bus Singleton | Makes unit testing perception logic more difficult. |

---

## 5. Improvement Roadmap

### Phase 1: Engine Decentralization (High Priority)
*   **Task**: Move secondary behaviors to specialized animator/executor sub-systems.
*   **Complexity**: Medium.
*   **Risk**: Low (State logic remains the same).
*   **Impact**: Better maintainability and testability.

### Phase 2: Renderer Refinement & Optimization (High Priority)
*   **Task**: Extract hardcoded constants to a `LuxTheme` or `EyeConstants` object.
*   **Task**: Implement multi-path caching in `EyePathBuilder`.
*   **Complexity**: Low.
*   **Risk**: Low.
*   **Impact**: Performance stability and easier visual tuning.

### Phase 3: Hardware Realism (Medium Priority)
*   **Task**: Optimize Raster lines using a `Brush` or `Shader`.
*   **Task**: Add subtle "scanning" artifacts and visor parallax.
*   **Complexity**: Medium.
*   **Risk**: Medium (Performance).
*   **Impact**: Significant visual "believability" boost.

### Phase 4: Cognitive Expansion (Design Only)
*   **Task**: Finalize the v4 transition plan for memory and motivation models.
*   **Complexity**: High.
*   **Risk**: High (Architectural change).
*   **Impact**: Character depth.
