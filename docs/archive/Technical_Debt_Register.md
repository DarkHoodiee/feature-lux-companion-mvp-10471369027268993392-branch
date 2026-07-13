# Technical Debt Register — LUX Hoodie

This register tracks "shortcuts" and legacy artifacts that hinder maintainability or visual refinement.

---

## 1. High Priority (Refactor Soon)

| Item | Impact | Description |
| :--- | :--- | :--- |
| **Renderer Constants** | High | Geometry spacing, look offsets, and bloom stroke widths are hardcoded in `LuxFaceRenderer.kt`. |
| **Engine Coupling** | High | `FaceAnimator` is tightly bound to `CoroutineScope` and `MutableStateFlow` from the Engine. |
| **Mock Logic** | Med | `BehaviorExecutor` uses empty methods for `runIdleObserve` and `runInvestigate`. |

---

## 2. Medium Priority (Maintenance)

| Item | Impact | Description |
| :--- | :--- | :--- |
| **Path Cache Limit** | Med | `EyePathBuilder` only caches the most recent path, leading to thrashing during asymmetric blinks. |
| **Event Bus Singleton** | Med | Global singleton `EventBus` makes isolated unit testing of the perception layer difficult. |
| **Manual Loop Scaling** | Low | `drawRasterLines` uses a `while` loop instead of a more efficient `Brush` or `Shader`. |
