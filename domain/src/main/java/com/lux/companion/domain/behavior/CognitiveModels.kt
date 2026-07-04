package com.lux.companion.domain.behavior

/**
 * High-level emotional and cognitive drivers.
 */
enum class InternalMotivation {
    CURIOSITY,      // Driven by new events or high focus
    REST,           // Driven by low focus or inactivity
    VIGILANCE,      // Driven by interaction
    BOREDOM,        // Driven by long periods of same state
    INITIALIZING    // Hardware startup
}
