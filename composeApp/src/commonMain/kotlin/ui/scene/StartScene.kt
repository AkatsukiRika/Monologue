package ui.scene

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import global.Global
import global.Routes
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import playAudioFile
import ui.common.AppColors
import ui.common.AppFonts
import ui.common.CommonButton
import ui.common.PixelText
import kotlin.system.exitProcess

private const val AUDIO_BGM_TITLE = "title_bgm.mp3"

@OptIn(ExperimentalResourceApi::class)
@Composable
fun StartScene(navigator: Navigator) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource("drawable/img_title_background.webp"),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        MainColumn(navigator = navigator)
    }

    LaunchedEffect(Unit) {
        playAudioFile(fileName = AUDIO_BGM_TITLE, loop = true)
    }
}

@Composable
private fun MainColumn(navigator: Navigator) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(128f))

        Text(
            text = Global.Strings.title,
            style = TextStyle(
                fontFamily = AppFonts.MSGothic(),
                color = Color.White,
                fontSize = 52.sp
            )
        )

        Text(
            text = Global.Strings.titleEN,
            style = TextStyle(
                fontFamily = AppFonts.MSGothic(),
                color = Color.White,
                fontSize = 26.sp
            )
        )

        Spacer(modifier = Modifier.weight(112f))

        CommonButton(
            onClick = {
                navigator.navigate(
                    route = Routes.Game,
                    options = NavOptions(launchSingleTop = true)
                )
            },
            strokeWidth = 2.dp,
            modifier = Modifier
                .size(width = 200.dp, height = 48.dp)
                .background(AppColors.Color_C4C7CB)
        ) {
            PixelText(
                text = Global.Strings.startOver,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.weight(16f))

        CommonButton(
            onClick = {
                navigator.navigate(
                    route = Routes.Game,
                    options = NavOptions(launchSingleTop = true)
                )
            },
            strokeWidth = 2.dp,
            modifier = Modifier
                .size(width = 200.dp, height = 48.dp)
                .background(AppColors.Color_C4C7CB)
        ) {
            PixelText(
                text = Global.Strings.continueGame,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.weight(16f))

        CommonButton(
            onClick = {
                navigator.navigate(
                    route = Routes.Settings,
                    options = NavOptions(launchSingleTop = true)
                )
            },
            strokeWidth = 2.dp,
            modifier = Modifier
                .size(width = 200.dp, height = 48.dp)
                .background(AppColors.Color_C4C7CB)
        ) {
            PixelText(
                text = Global.Strings.settings,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.weight(16f))

        CommonButton(
            onClick = { exitProcess(status = 0) },
            strokeWidth = 2.dp,
            modifier = Modifier
                .size(width = 200.dp, height = 48.dp)
                .background(AppColors.Color_C4C7CB)
        ) {
            PixelText(
                text = Global.Strings.exitGame,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.weight(80f))
    }
}