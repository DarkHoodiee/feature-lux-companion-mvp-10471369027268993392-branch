package com.lux.companion.engine

import kotlin.math.sqrt

/**
 * A simple spring-physics solver for smooth, organic motion.
 * Provides overshoot and settling effects.
 */
class SpringSolver(
    var stiffness: Float = 150f,
    var dampingRatio: Float = 0.7f
) {
    private var velocity: Float = 0f

    fun next(current: Float, target: Float, dt: Float): Float {
        val displacement = current - target

        // Critical damping calculation: damping = 2 * sqrt(stiffness) * dampingRatio
        val damping = 2f * sqrt(stiffness) * dampingRatio

        val springForce = -stiffness * displacement
        val dampingForce = -damping * velocity
        val acceleration = springForce + dampingForce

        velocity += acceleration * dt
        return current + velocity * dt
    }

    fun isSettled(current: Float, target: Float, threshold: Float = 0.01f): Boolean {
        return kotlin.math.abs(current - target) < threshold && kotlin.math.abs(velocity) < threshold
    }

    fun resetVelocity() {
        velocity = 0f
    }
}
