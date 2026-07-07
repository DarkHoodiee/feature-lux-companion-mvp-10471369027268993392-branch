package com.lux.hoodie.behavior

import com.lux.hoodie.animation.FaceAnimator
import com.lux.hoodie.domain.Intention
import com.lux.hoodie.domain.LuxExpression
import com.lux.hoodie.domain.toTopology
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.coroutineScope
import kotlin.random.Random

interface AnimatorIntention : Intention {
    suspend fun executeWithAnimators(left: FaceAnimator, right: FaceAnimator)
}

class InvestigateIntention : AnimatorIntention {
    override val name = "Investigate"
    override suspend fun execute() {}

    override suspend fun executeWithAnimators(left: FaceAnimator, right: FaceAnimator) {
        coroutineScope {
            launch { left.animateTo(LuxExpression.FOCUSED.toTopology()) }
            launch { right.animateTo(LuxExpression.FOCUSED.toTopology()) }
            delay(800)
            launch { left.animateTo(LuxExpression.CURIOUS.toTopology()) }
            launch { right.animateTo(LuxExpression.CURIOUS.toTopology()) }
            delay(1200)
            launch { left.animateTo(LuxExpression.NEUTRAL.toTopology()) }
            launch { right.animateTo(LuxExpression.NEUTRAL.toTopology()) }
        }
    }
}

class WakeUpIntention : AnimatorIntention {
    override val name = "WakeUp"
    override suspend fun execute() {}

    override suspend fun executeWithAnimators(left: FaceAnimator, right: FaceAnimator) {
        coroutineScope {
            val surprised = LuxExpression.NEUTRAL.toTopology().copy(heightScale = 1.3f, widthScale = 1.1f)
            launch { left.animateTo(surprised) }
            launch { right.animateTo(surprised) }
            delay(1000)
            launch { left.animateTo(LuxExpression.NEUTRAL.toTopology()) }
            launch { right.animateTo(LuxExpression.NEUTRAL.toTopology()) }
        }
    }
}

class IdleObserveIntention : AnimatorIntention {
    override val name = "IdleObserve"
    override suspend fun execute() {}

    override suspend fun executeWithAnimators(left: FaceAnimator, right: FaceAnimator) {
        coroutineScope {
            val neutral = LuxExpression.NEUTRAL.toTopology()
            val micro = neutral.copy(
                softness = 0.95f + Random.nextFloat() * 0.1f,
                heightScale = 0.98f + Random.nextFloat() * 0.04f,
                upperLidInset = Random.nextFloat() * 0.05f
            )
            launch { left.animateTo(micro) }
            launch { right.animateTo(micro) }
            delay(1500 + Random.nextLong(1500))
            launch { left.animateTo(neutral) }
            launch { right.animateTo(neutral) }
        }
    }
}

class CuriosityTiltIntention : AnimatorIntention {
    override val name = "CuriosityTilt"
    override suspend fun execute() {}

    override suspend fun executeWithAnimators(left: FaceAnimator, right: FaceAnimator) {
        coroutineScope {
            val leftTilt = LuxExpression.CURIOUS.toTopology().copy(cornerPinch = 0.3f)
            val rightTilt = LuxExpression.CURIOUS.toTopology().copy(cornerPinch = -0.1f)
            launch { left.animateTo(leftTilt) }
            launch { right.animateTo(rightTilt) }
            delay(2000)
            launch { left.animateTo(LuxExpression.NEUTRAL.toTopology()) }
            launch { right.animateTo(LuxExpression.NEUTRAL.toTopology()) }
        }
    }
}

class DrowseIntention : AnimatorIntention {
    override val name = "Drowse"
    override suspend fun execute() {}

    override suspend fun executeWithAnimators(left: FaceAnimator, right: FaceAnimator) {
        coroutineScope {
            val sleepy = LuxExpression.SLEEPY.toTopology().copy(upperLidInset = 0.4f)
            launch { left.animateTo(sleepy) }
            launch { right.animateTo(sleepy) }
            delay(3000)
            launch { left.animateTo(LuxExpression.NEUTRAL.toTopology()) }
            launch { right.animateTo(LuxExpression.NEUTRAL.toTopology()) }
        }
    }
}

class LookAtIntention(private val x: Float, private val y: Float) : AnimatorIntention {
    override val name = "LookAt"
    override suspend fun execute() {}

    override suspend fun executeWithAnimators(left: FaceAnimator, right: FaceAnimator) {
        coroutineScope {
            launch { left.animateTo(LuxExpression.FOCUSED.toTopology()) }
            launch { right.animateTo(LuxExpression.FOCUSED.toTopology()) }
            delay(1000)
            launch { left.animateTo(LuxExpression.NEUTRAL.toTopology()) }
            launch { right.animateTo(LuxExpression.NEUTRAL.toTopology()) }
        }
    }
}
