# Remaining Technical Debt — LUX Hoodie (v3.1)

While the v3.1 Baseline is stable, the following "shortcuts" persist and should be addressed in the v3.2/Hardening phase.

---

## 1. High Priority
| Item | Description |
| :--- | :--- |
| **Renderer Hardcoding** | Eye spacing (160f) and bloom widths are still hardcoded in `LuxFaceRenderer.kt`. Should be moved to a `LuxTheme` or `EyeConstants` file. |
| **State Persistence** | Memory and Mood data are transient. A DataStore implementation is needed to persist character state across app restarts. |

---

## 2. Medium Priority
| Item | Description |
| :--- | :--- |
| **Raster Performance** | `drawRasterLines` uses a `while` loop. Optimization using a `Shader` or `Bitmap` brush is recommended for high-refresh-rate stability. |
| **Path Cache Collision** | `EyePathBuilder` only caches the most recent path. Asymmetric blinks (where eyes have different topology) trigger re-allocations. |

---

## 3. Low Priority
| Item | Description |
| :--- | :--- |
| **Event Bus Singleton** | Global `EventBus` works well but makes isolated unit testing of the perception layer more difficult than a DI-based approach. |
