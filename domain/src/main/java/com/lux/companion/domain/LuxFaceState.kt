package com.lux.companion.domain

enum class LuxMood {
    CALM,
    ENERGETIC,
    TIRED,
    INQUISITIVE,
    PLAYFUL
}

data class LuxFaceState(
    val startupPhase: StartupPhase = StartupPhase.OFF,
    val startupProgress: Float = 0f, // 0f to 1f within a phase
    val leftEye: EyeState = EyeState(),
    val rightEye: EyeState = EyeState(),
    val mood: LuxMood = LuxMood.CALM,
    val isScanning: Boolean = false,
    val scanProgress: Float = 0f,
    val isRefreshing: Boolean = false,
    val refreshProgress: Float = 0f,
    val verticalOffset: Float = 0f, // For breathing/floating
    val rotationZ: Float = 0f       // For floating/tilting
)
