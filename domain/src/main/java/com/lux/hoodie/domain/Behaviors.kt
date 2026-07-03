package com.lux.hoodie.domain

/**
 * High-level behavioral intentions.
 */
interface Intention {
    val name: String
    suspend fun execute() {}
}

/**
 * Semantic expressions that map to specific topologies.
 */
enum class LuxExpression {
    NEUTRAL,
    CURIOUS,
    FOCUSED,
    SLEEPY,
    HAPPY,
    SURPRISED,
    THINKING,
    RELAXED
}
