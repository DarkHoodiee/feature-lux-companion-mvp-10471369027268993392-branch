package com.lux.companion

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import com.lux.companion.domain.LuxInteraction
import com.lux.companion.renderer.LuxFaceCanvas

@Composable
fun LuxFaceScreen(viewModel: LuxViewModel) {
    val state by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF001219)) // Deep space blue/black
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { offset -> viewModel.onInteraction(LuxInteraction.Tap(offset.x, offset.y)) },
                    onDoubleTap = { offset -> viewModel.onInteraction(LuxInteraction.DoubleTap(offset.x, offset.y)) },
                    onLongPress = { offset -> viewModel.onInteraction(LuxInteraction.LongPress(offset.x, offset.y)) }
                )
            }
    ) {
        LuxFaceCanvas(state = state)
    }
}
