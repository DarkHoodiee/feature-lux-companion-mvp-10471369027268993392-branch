package com.lux.companion.domain

enum class LuxMood {
    CALM,
    ENERGETIC,
    TIRED,
    INQUISITIVE,
    PLAYFUL
}

enum class StartupPhase {
    OFF,
    CENTER_DOT,           // Phase 1: Dot appears
    EXPAND_LINE,          // Phase 2: Dot expands to line
    SWEEP_UP_DISAPPEAR,   // Phase 3: Line sweeps up and disappears
    BOTTOM_LINE_APPEAR,   // Phase 4: New line appears at bottom
    SWEEP_UP_REVEAL,      // Phase 5: Sweeps up, eyes materialize
    COMPLETE              // Phase 6: Idle state
}

data class LuxFaceState(
    val leftEye: EyeState = EyeState(),
    val rightEye: EyeState = EyeState(),
    val mood: LuxMood = LuxMood.CALM,
    val isScanning: Boolean = false,
    val scanProgress: Float = 0f,
    val isRefreshing: Boolean = false,
    val refreshProgress: Float = 0f,
    val startupPhase: StartupPhase = StartupPhase.OFF,
    val startupProgress: Float = 0f,
    val verticalOffset: Float = 0f, // For breathing/floating
    val rotationZ: Float = 0f       // For floating/tilting
)
