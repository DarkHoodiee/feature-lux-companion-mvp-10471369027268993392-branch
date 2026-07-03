package com.lux.hoodie.behavior

import com.lux.hoodie.animation.FaceAnimator
import com.lux.hoodie.domain.Intention
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Executes intentions and manages the lifecycle of running behaviors.
 */
class BehaviorExecutor(
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default),
    private val leftAnimator: FaceAnimator,
    private val rightAnimator: FaceAnimator
) {
    private var activeJob: Job? = null

    fun execute(intention: Intention) {
        activeJob?.cancel()
        activeJob = scope.launch {
            if (intention is AnimatorIntention) {
                intention.executeWithAnimators(leftAnimator, rightAnimator)
            } else {
                intention.execute()
            }
        }
    }
}
