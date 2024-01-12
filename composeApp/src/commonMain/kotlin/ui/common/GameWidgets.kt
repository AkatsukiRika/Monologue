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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun NormalText(modifier: Modifier = Modifier, state: GameState) {
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
            text = state.currentText.replace("\\n", "\n"),
            fontSize = 20.sp,
            color = Color.White,
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp)
                .align(Alignment.Center)
        )
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

            NormalText(state = state)
        }

        DescText(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 16.dp, end = 16.dp),
            text = state.timeText
        )
    }
}