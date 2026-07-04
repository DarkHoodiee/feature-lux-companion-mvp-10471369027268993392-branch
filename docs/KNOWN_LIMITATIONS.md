# Known Limitations & Deferred Items — LUX Hoodie v3.1

The current baseline is a functional character prototype. The following systems are intentionally deferred for future cognitive versions (v4.0+).

## 1. Cognitive Limitations
*   **Long-term Memory**: LUX does not yet remember user behavior across app restarts or days.
*   **Complex Planning**: Goals are currently short-term (Reactive or Active sequences). There is no high-level planning (e.g., "Daily Routines").
*   **Personality Evolution**: The trait weights are currently static; they do not yet shift based on user engagement.
*   **Speech & Voice**: LUX is currently a silent character. Speech synthesis and robotic tones are planned for v5.0.

## 2. Rendering Limitations
*   **Head Shell**: The white glossy outer shell is not yet implemented. The visor currently occupies the full screen.
*   **Curved Visor Depth**: Fresnel reflections and complex glass curvature are pending shader implementation.
*   **Environment Lighting**: The character does not yet react to ambient light or device orientation.

## 3. Platform Limitations
*   **Portrait Only**: The renderer is optimized for portrait mobile displays.
*   **No Multi-Character**: Only one LUX instance can be rendered at a time.
*   **Battery Saver**: LUX does not yet adjust her update frequency or glow intensity based on system battery levels.

## 4. Interaction Gaps
*   **Multi-touch**: LUX currently processes single tap gestures. Gesture-based interactions (swipe to pet, pinch to shrink) are not yet implemented.
*   **Sensors**: Gaze is purely internal/touch-driven. Camera-based attention (face tracking) is not implemented.
