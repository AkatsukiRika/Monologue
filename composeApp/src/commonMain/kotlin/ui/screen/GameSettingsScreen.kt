package ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import global.Global
import ui.common.AppColors
import ui.common.CommonButton
import ui.common.PixelText

@Composable
fun GameSettingsScreen(onBackToMenu: () -> Unit, onEnd: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.75f))
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {}
    ) {
        MainColumn(
            modifier = Modifier.align(Alignment.Center),
            onBackToMenu = onBackToMenu,
            onEnd = onEnd
        )
    }
}

@Composable
private fun MainColumn(
    modifier: Modifier = Modifier,
    onBackToMenu: () -> Unit,
    onEnd: () -> Unit
) {
    Column(modifier = modifier) {
        CommonButton(
            onClick = {},
            modifier = Modifier
                .width(144.dp)
                .height(48.dp)
                .background(AppColors.Color_C4C7CB),
            strokeWidth = 2.dp
        ) {
            PixelText(
                text = Global.Strings.save,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        CommonButton(
            onClick = {},
            modifier = Modifier
                .width(144.dp)
                .height(48.dp)
                .background(AppColors.Color_C4C7CB),
            strokeWidth = 2.dp
        ) {
            PixelText(
                text = Global.Strings.load,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        CommonButton(
            onClick = onBackToMenu,
            modifier = Modifier
                .width(144.dp)
                .height(48.dp)
                .background(AppColors.Color_C4C7CB),
            strokeWidth = 2.dp
        ) {
            PixelText(
                text = Global.Strings.backToTitle,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        CommonButton(
            onClick = onEnd,
            modifier = Modifier
                .width(144.dp)
                .height(48.dp)
                .background(AppColors.Color_C4C7CB),
            strokeWidth = 2.dp
        ) {
            PixelText(
                text = Global.Strings.exitGame,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}