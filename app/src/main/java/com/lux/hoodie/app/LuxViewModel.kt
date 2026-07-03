package com.lux.hoodie.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lux.hoodie.behavior.BehaviorExecutor
import com.lux.hoodie.behavior.BehaviorPlanner
import com.lux.hoodie.domain.LuxFaceState
import com.lux.hoodie.engine.AutonomousEngine
import com.lux.hoodie.events.EventBus
import com.lux.hoodie.perception.PerceptionLayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LuxViewModel : ViewModel() {
    private val engine = AutonomousEngine()
    private val planner = BehaviorPlanner()
    private val executor = BehaviorExecutor(
        scope = viewModelScope,
        leftAnimator = engine.leftEyeAnimator,
        rightAnimator = engine.rightEyeAnimator
    )
    private val perception = PerceptionLayer(viewModelScope)

    private val _faceState = MutableStateFlow(LuxFaceState())
    val faceState: StateFlow<LuxFaceState> = _faceState.asStateFlow()

    init {
        viewModelScope.launch {
            EventBus.events.collect { event ->
                planner.planFromEvent(event)?.let { intention ->
                    executor.execute(intention)
                }
            }
        }

        // Continuous state update loop from animators to faceState
        viewModelScope.launch {
            while (true) {
                _faceState.value = LuxFaceState(
                    leftEye = engine.leftEyeAnimator.currentTopology(),
                    rightEye = engine.rightEyeAnimator.currentTopology()
                )
                kotlinx.coroutines.delay(16)
            }
        }
    }

    fun onScreenInteraction(x: Float, y: Float) {
        perception.onTouch(x, y)
    }
}
