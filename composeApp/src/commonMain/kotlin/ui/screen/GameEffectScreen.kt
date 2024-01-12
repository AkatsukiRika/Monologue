package ui.screen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import models.GameTypes

@Composable
fun GameEffectScreen(type: String, onAfterEffect: () -> Unit) {
    var bgColor by remember { mutableStateOf(Color.Transparent) }
    val scope = rememberCoroutineScope()
    val onRedTurn = {
        scope.launch {
            bgColor = Color.Red
            delay(200)
            bgColor = Color.Transparent
            onAfterEffect()
        }
    }
    val onClimaxBlink = {
        scope.launch {
            bgColor = Color.White.copy(alpha = 0f)
            repeat(times = 2) {
                animate(
                    initialValue = 0f,
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 300, easing = LinearEasing)
                ) { value, _ ->
                    bgColor = Color.White.copy(alpha = value)
                }
                animate(
                    initialValue = 1f,
                    targetValue = 0f,
                    animationSpec = tween(durationMillis = 200, easing = LinearEasing)
                ) { value, _ ->
                    bgColor = Color.White.copy(alpha = value)
                }
                delay(200)
            }
            bgColor = Color.Transparent
            onAfterEffect()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
    )

    LaunchedEffect(type) {
        when (type) {
            GameTypes.Effect.RedTurn -> {
                onRedTurn()
            }
            GameTypes.Effect.ClimaxBlink -> {
                onClimaxBlink()
            }
        }
    }
}