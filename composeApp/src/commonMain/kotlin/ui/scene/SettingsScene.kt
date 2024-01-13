package ui.scene

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import global.Global
import moe.tlaster.precompose.navigation.Navigator
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import ui.common.AppColors
import ui.common.CommonButton
import ui.common.PixelText
import kotlin.system.exitProcess

@Composable
fun SettingsScene(navigator: Navigator) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Color_56AAAA)
    ) {
        Spacer(modifier = Modifier.height(64.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            ItemName(
                name = Global.Strings.textSpeed,
                modifier = Modifier.padding(start = 40.dp)
            )

            Spacer(modifier = Modifier.width(56.dp))

            TextSpeedRow()
        }

        Spacer(modifier = Modifier.height(36.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            ItemName(
                name = Global.Strings.screenEffect,
                modifier = Modifier.padding(start = 40.dp)
            )

            Spacer(modifier = Modifier.width(56.dp))

            ScreenEffectRow()
        }

        Spacer(modifier = Modifier.height(36.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            ItemName(
                name = Global.Strings.audioVolume,
                modifier = Modifier.padding(start = 40.dp)
            )

            Spacer(modifier = Modifier.width(56.dp))

            AudioVolumeRow(currentVolume = 10)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.width(132.dp))

            SaveButton()

            Spacer(modifier = Modifier.width(160.dp))

            LoadButton()
        }

        Spacer(modifier = Modifier.height(80.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            CommonButton(
                onClick = {},
                modifier = Modifier
                    .width(156.dp)
                    .height(48.dp)
                    .background(AppColors.Color_C4C7CB),
                strokeWidth = 2.dp
            ) {
                PixelText(
                    text = Global.Strings.backToGame,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.width(56.dp))

            CommonButton(
                onClick = {
                    navigator.goBack()
                },
                modifier = Modifier
                    .width(156.dp)
                    .height(48.dp)
                    .background(AppColors.Color_C4C7CB),
                strokeWidth = 2.dp
            ) {
                PixelText(
                    text = Global.Strings.backToTitle,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.width(56.dp))

            CommonButton(
                onClick = {
                    exitProcess(status = 0)
                },
                modifier = Modifier
                    .width(156.dp)
                    .height(48.dp)
                    .background(AppColors.Color_C4C7CB),
                strokeWidth = 2.dp
            ) {
                PixelText(
                    text = Global.Strings.exitGame,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )
            }
        }
    }
}

@Composable
private fun ItemName(
    modifier: Modifier = Modifier,
    name: String
) {
    Box(
        modifier = modifier
            .width(180.dp)
            .height(48.dp)
            .background(AppColors.Color_808080),
        contentAlignment = Alignment.Center
    ) {
        PixelText(
            text = name,
            color = Color.White,
            fontSize = 20.sp
        )
    }
}

@Composable
private fun TextSpeedRow(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PixelText(
            text = Global.Strings.speedSlow,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.width(12.dp))
        DivideLine()
        Spacer(modifier = Modifier.width(12.dp))

        PixelText(
            text = Global.Strings.speedNormal,
            fontSize = 20.sp,
            color = Color.White
        )

        Spacer(modifier = Modifier.width(12.dp))
        DivideLine()
        Spacer(modifier = Modifier.width(12.dp))

        PixelText(
            text = Global.Strings.speedFast,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.width(12.dp))
        DivideLine()
        Spacer(modifier = Modifier.width(12.dp))

        PixelText(
            text = Global.Strings.speedFastest,
            fontSize = 20.sp
        )
    }
}

@Composable
private fun ScreenEffectRow(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PixelText(
            text = Global.Strings.on,
            fontSize = 20.sp,
            color = Color.White
        )

        Spacer(modifier = Modifier.width(12.dp))
        DivideLine()
        Spacer(modifier = Modifier.width(12.dp))

        PixelText(
            text = Global.Strings.off,
            fontSize = 20.sp
        )
    }
}

@Composable
private fun AudioVolumeRow(modifier: Modifier = Modifier, currentVolume: Int) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        VolumeMinusIcon()

        Spacer(modifier = Modifier.width(32.dp))

        PixelText(
            text = currentVolume.toString(),
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.width(32.dp))

        VolumePlusIcon()
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun DivideLine() {
    Image(
        painter = painterResource("drawable/icon_divide_line.png"),
        contentDescription = null
    )
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun VolumeMinusIcon() {
    Image(
        painter = painterResource("drawable/icon_triangle_left.png"),
        contentDescription = null
    )
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun VolumePlusIcon() {
    Image(
        painter = painterResource("drawable/icon_triangle_right.png"),
        contentDescription = null
    )
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun SaveButton() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource("drawable/icon_save.png"),
            contentDescription = null
        )

        Spacer(modifier = Modifier.width(20.dp))

        PixelText(
            text = Global.Strings.save,
            fontSize = 20.sp
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun LoadButton() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource("drawable/icon_load.png"),
            contentDescription = null
        )

        Spacer(modifier = Modifier.width(20.dp))

        PixelText(
            text = Global.Strings.load,
            fontSize = 20.sp
        )
    }
}