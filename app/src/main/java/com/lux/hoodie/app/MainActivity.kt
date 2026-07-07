package com.lux.hoodie.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.lux.hoodie.renderer.LuxFaceRenderer
import com.lux.hoodie.renderer.StartupSequence

class MainActivity : ComponentActivity() {

    private val viewModel: LuxViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var showStartup by remember { mutableStateOf(true) }
            val faceState by viewModel.faceState.collectAsState()

            if (showStartup) {
                StartupSequence(onComplete = { showStartup = false })
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectTapGestures { offset ->
                                viewModel.onScreenInteraction(offset.x, offset.y)
                            }
                        }
                ) {
                    LuxFaceRenderer(state = faceState)
                }
            }
        }
    }
}
