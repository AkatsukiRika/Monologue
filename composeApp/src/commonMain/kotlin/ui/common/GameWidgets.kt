package ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import global.Global
import kotlinx.coroutines.delay
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import ui.vm.GameState
import utils.sideBorders

@Composable
fun SpecialWhiteText(modifier: Modifier = Modifier, state: GameState) {
    PixelText(
        text = state.currentText.replace("\\n", "\n"),
        fontSize = 32.sp,
        color = Color.White,
        modifier = modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}

@Composable
fun SpecialRedText(modifier: Modifier = Modifier, state: GameState) {
    Text(
        text = state.currentText.replace("\\n", "\n"),
        fontFamily = AppFonts.MSGothic(),
        fontSize = 32.sp,
        color = Color.Red,
        modifier = modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}

@Composable
fun NormalText(
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    centerText: Boolean = false,
    lineHeight: TextUnit? = null,
    state: GameState,
    onUpdateTextAnimStatus: ((isPlaying: Boolean) -> Unit)? = null,
    skipTextAnim: (() -> Int)? = null
) {
    var currentText by remember { mutableStateOf("") }
    val lastSkipAnim = Global.skipTextAnimCountFlow.collectAsStateWithLifecycle().value
    val skipAnim = skipTextAnim?.invoke() ?: 0

    Box(
        modifier = modifier
            .background(AppColors.Color_808080)
            .sideBorders(
                colorTop = Color.White,
                colorStart = Color.White,
                colorBottom = Color.Black,
                colorEnd = Color.Black,
                strokeWidth = 2.dp
            )
            .fillMaxWidth()
            .height(128.dp)
    ) {
        PixelText(
            text = currentText,
            fontSize = 20.sp,
            color = if (state.showSpecialYellowText) Color.Yellow else Color.White,
            lineHeight = lineHeight,
            modifier = Modifier
                .align(if (centerText) Alignment.Center else Alignment.TopStart)
                .then(textModifier)
        )
    }

    LaunchedEffect(state.currentText, skipAnim) {
        if (skipAnim > 0 && skipAnim > lastSkipAnim) {
            currentText = state.currentText.replace("\\n", "\n")
            onUpdateTextAnimStatus?.invoke(false)
            Global.skipTextAnimCountFlow.emit(skipAnim)
        } else {
            onUpdateTextAnimStatus?.invoke(true)
            currentText = ""
            val fullText = state.currentText.replace("\\n", "\n")
            fullText.forEach {
                currentText += it
                delay(Global.textSpeedMillis)
            }
            onUpdateTextAnimStatus?.invoke(false)
        }
    }
}

@Composable
fun ScaryText(
    modifier: Modifier,
    textModifier: Modifier = Modifier,
    lineHeight: TextUnit? = null,
    state: GameState,
    onUpdateTextAnimStatus: ((isPlaying: Boolean) -> Unit)? = null,
    skipTextAnim: (() -> Int)? = null
) {
    var currentText by remember { mutableStateOf("") }
    val lastSkipAnim = Global.skipTextAnimCountFlow.collectAsStateWithLifecycle().value
    val skipAnim = skipTextAnim?.invoke() ?: 0

    Box(
        modifier = modifier
            .background(Color.Black)
            .fillMaxWidth()
            .height(128.dp)
    ) {
        PixelText(
            text = currentText,
            fontSize = 20.sp,
            color = Color.Red,
            lineHeight = lineHeight,
            modifier = Modifier
                .align(Alignment.TopStart)
                .then(textModifier)
        )
    }

    LaunchedEffect(state.currentText, skipAnim) {
        if (skipAnim > 0 && skipAnim > lastSkipAnim) {
            currentText = state.currentText.replace("\\n", "\n")
            onUpdateTextAnimStatus?.invoke(false)
            Global.skipTextAnimCountFlow.emit(skipAnim)
        } else {
            onUpdateTextAnimStatus?.invoke(true)
            currentText = ""
            val fullText = state.currentText.replace("\\n", "\n")
            fullText.forEach {
                currentText += it
                delay(Global.textSpeedMillis)
            }
            onUpdateTextAnimStatus?.invoke(false)
        }
    }
}

@Composable
fun DescText(modifier: Modifier = Modifier, text: String) {
    Box(
        modifier = modifier
            .width(128.dp)
            .height(36.dp)
            .background(AppColors.Color_C0C0C0)
            .sideBorders(
                colorTop = Color.White,
                colorStart = Color.White,
                colorBottom = Color.Black,
                colorEnd = Color.Black,
                strokeWidth = 2.dp
            )
    ) {
        PixelText(
            text = text,
            fontSize = 20.sp,
            color = Color.Black,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun SpeechLayout(state: GameState) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            DescText(text = state.characterName)

            Spacer(modifier = Modifier.height(16.dp))

            NormalText(state = state, centerText = true)
        }

        DescText(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 16.dp, end = 16.dp),
            text = state.timeText
        )
    }
}