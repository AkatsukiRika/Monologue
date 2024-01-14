package ui.screen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import models.GameTypes
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import ui.vm.GameState

private val invertColorMatrix = floatArrayOf(
    -1f, 0f, 0f, 0f, 255f,
    0f, -1f, 0f, 0f, 255f,
    0f, 0f, -1f, 0f, 255f,
    0f, 0f, 0f, 1f, 0f
)

@OptIn(ExperimentalResourceApi::class)
@Composable
fun GameEffectScreen(state: GameState, type: String, onAfterEffect: () -> Unit) {
    var bgColor by remember { mutableStateOf(Color.Transparent) }
    var invertAlpha by remember { mutableFloatStateOf(0f) }
    val scope = rememberCoroutineScope()
    val onRedTurn = {
        scope.launch {
            bgColor = Color.Red
            delay(200)
            bgColor = Color.Transparent
            onAfterEffect()
        }
    }
    val onTurnWhite = {
        scope.launch {
            bgColor = Color.White.copy(alpha = 0f)
            animate(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = tween(durationMillis = 500, easing = LinearEasing)
            ) { value, _ ->
                bgColor = Color.White.copy(alpha = value)
            }
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
    val onInvertColor = {
        scope.launch {
            animate(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
            ) { value, _ ->
                invertAlpha = value
            }
            invertAlpha = 0f
            onAfterEffect()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
    ) {
        val backImage = GameTypes.Back.getImage(state.currentBack)
        if (invertAlpha > 0f && backImage != null) {
            Image(
                painter = painterResource(backImage),
                contentDescription = null,
                colorFilter = ColorFilter.colorMatrix(ColorMatrix(invertColorMatrix)),
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        alpha = invertAlpha
                    },
                contentScale = ContentScale.Crop
            )
        }
    }

    LaunchedEffect(type) {
        when (type) {
            GameTypes.Effect.RedTurn -> {
                onRedTurn()
            }
            GameTypes.Effect.ClimaxBlink -> {
                onClimaxBlink()
            }
            GameTypes.Effect.InvertColor -> {
                onInvertColor()
            }
            GameTypes.Effect.TurnWhite -> {
                onTurnWhite()
            }
        }
    }
}