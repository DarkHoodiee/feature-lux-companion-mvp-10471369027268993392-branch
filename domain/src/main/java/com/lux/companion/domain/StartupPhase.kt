package com.lux.companion.domain

/**
 * Defines the 6-phase cinematic hardware initialization sequence for LUX.
 */
enum class StartupPhase {
    OFF,
    CENTER_DOT,           // Single luminous point at center
    HORIZONTAL_EXPANSION, // Dot expands to a full horizontal line
    SWEEP_UP_TOP,         // Line sweeps from center to the top edge
    NEW_LINE_BOTTOM,      // A new line appears at the bottom
    SWEEP_UP_REVEAL,      // Bottom line sweeps up to reveal the eyes
    ONLINE                // Transition to Idle Brain
}
