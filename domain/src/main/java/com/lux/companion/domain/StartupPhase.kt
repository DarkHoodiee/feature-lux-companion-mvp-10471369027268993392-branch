package com.lux.companion.domain

/**
 * Defines the 7-phase hardware initialization sequence for LUX.
 */
enum class StartupPhase {
    OFF,
    BOOT_DOT,           // Single luminous point at center
    EXPANSION_LINE,     // Dot expands horizontally
    UPPER_SWEEP,        // Line sweeps to top
    LOWER_SWEEP,        // New line from bottom sweeps to center
    EYE_MATERIALIZING,  // Eyes emerge as lower sweep finishes
    ONLINE              // Transition to Idle Brain
}
