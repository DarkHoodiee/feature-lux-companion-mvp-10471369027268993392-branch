package com.lux.companion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lux.companion.domain.LuxFaceState
import com.lux.companion.domain.LuxInteraction
import com.lux.companion.engine.AutonomousEngine
import com.lux.companion.interaction.InteractionHandler
import kotlinx.coroutines.flow.StateFlow

class LuxViewModel : ViewModel() {

    private val engine = AutonomousEngine(viewModelScope)

    val uiState: StateFlow<LuxFaceState> = engine.state

    private val interactionHandler = InteractionHandler { expression ->
        engine.updateExpression(expression)
    }

    fun onInteraction(interaction: LuxInteraction) {
        interactionHandler.handleInteraction(interaction)
    }
}
