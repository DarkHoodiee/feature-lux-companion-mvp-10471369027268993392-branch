package com.lux.hoodie.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import com.lux.hoodie.domain.EyeTopology
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

/**
 * Handles smooth transitions between EyeTopology states using Compose Animatable.
 */
class FaceAnimator {
    // We animate individual parameters to allow for complex morphing if needed,
    // but for simplicity we can also animate the "fraction" or use a custom VectorConverter.
    // For this implementation, we'll animate a set of parameters that define the topology.

    val widthScale = Animatable(1f)
    val heightScale = Animatable(1f)
    val upperCurve = Animatable(0f)
    val lowerCurve = Animatable(0f)
    val innerCompression = Animatable(0f)
    val outerExpansion = Animatable(0f)
    val cornerPinch = Animatable(0f)
    val taper = Animatable(0f)
    val softness = Animatable(1f)
    val upperLidInset = Animatable(0f)
    val lowerLidInset = Animatable(0f)

    suspend fun animateTo(topology: EyeTopology) = coroutineScope {
        val spec = spring<Float>(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        )

        launch { widthScale.animateTo(topology.widthScale, spec) }
        launch { heightScale.animateTo(topology.heightScale, spec) }
        launch { upperCurve.animateTo(topology.upperCurve, spec) }
        launch { lowerCurve.animateTo(topology.lowerCurve, spec) }
        launch { innerCompression.animateTo(topology.innerCompression, spec) }
        launch { outerExpansion.animateTo(topology.outerExpansion, spec) }
        launch { cornerPinch.animateTo(topology.cornerPinch, spec) }
        launch { taper.animateTo(topology.taper, spec) }
        launch { softness.animateTo(topology.softness, spec) }
        launch { upperLidInset.animateTo(topology.upperLidInset, spec) }
        launch { lowerLidInset.animateTo(topology.lowerLidInset, spec) }
    }

    fun currentTopology() = EyeTopology(
        widthScale = widthScale.value,
        heightScale = heightScale.value,
        upperCurve = upperCurve.value,
        lowerCurve = lowerCurve.value,
        innerCompression = innerCompression.value,
        outerExpansion = outerExpansion.value,
        cornerPinch = cornerPinch.value,
        taper = taper.value,
        softness = softness.value,
        upperLidInset = upperLidInset.value,
        lowerLidInset = lowerLidInset.value
    )
}
