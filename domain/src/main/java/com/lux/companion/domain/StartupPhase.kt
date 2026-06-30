package com.lux.companion.domain

/**
 * The boot sequence phases:
 * a single point, widening into a line, sweeping to the top, then a
 * second line sweeping up from the bottom into center where the eyes resolve.
 */
enum class StartupPhase {
    OFF,
    BOOT_DOT,
    EXPANSION_LINE,
    UPPER_SWEEP,
    LOWER_SWEEP,
    EYE_MATERIALIZING,
    ONLINE
}
