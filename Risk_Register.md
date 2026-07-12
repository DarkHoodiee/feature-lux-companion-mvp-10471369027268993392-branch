# Risk Register — LUX Hoodie

This register tracks technical and visual risks that could impact the character's believability or application stability.

---

## 1. Technical Risks

| Risk | Impact | Probability | Mitigation |
| :--- | :---: | :---: | :--- |
| **Cognitive Infinite Loop** | High | Low | Implement intention timeouts in `BehaviorExecutor`. |
| **Path Cache Collision** | Med | Medium | Expand `EyePathBuilder` cache to support multiple keys. |
| **State Drift** | High | Med | Add periodic state verification in `AutonomousEngine`. |
| **OOM on 120Hz Displays** | Med | Low | Monitor allocation counts during complex morphs. |

---

## 2. Character Risks

| Risk | Impact | Probability | Mitigation |
| :--- | :---: | :---: | :--- |
| **Uncanny Valley** | Critical | High | Maintain non-linear spring physics; avoid linear movement. |
| **Repetitive Behavior** | High | High | Expand `IdleBrain` with "Habituation" models to reduce fatigue. |
| **Lack of Presence** | High | Med | Implement background "Micro-Adjustments" (1-2% probability). |
