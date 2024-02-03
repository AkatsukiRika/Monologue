package ui.scene

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import global.Global
import global.strings.BaseStrings
import models.GameTypes
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.viewmodel.viewModel
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import ui.common.AppColors
import ui.common.CommonButton
import ui.common.PixelText
import ui.vm.SettingsEvent
import ui.vm.SettingsState
import ui.vm.SettingsViewModel
import kotlin.system.exitProcess

@OptIn(ExperimentalResourceApi::class)
@Composable
fun SettingsScene(navigator: Navigator) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource("drawable/img_settings_bg.webp"),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(colors = listOf(
                    AppColors.Color_878A8E,
                    AppColors.Color_878A8E.copy(alpha = 0.8f),
                    Color.Transparent
                )))
        )

        MainColumn(navigator)
    }
}

@Composable
private fun MainColumn(navigator: Navigator) {
    val viewModel = viewModel(SettingsViewModel::class) {
        SettingsViewModel()
    }
    val state = viewModel.uiState.collectAsStateWithLifecycle().value
    val strings = Global.stringsFlow.collectAsStateWithLifecycle().value ?: Global.Strings

    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(32.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            ItemName(
                name = strings.textSpeed,
                modifier = Modifier.padding(start = 40.dp)
            )

            Spacer(modifier = Modifier.width(56.dp))

            TextSpeedRow(
                strings = strings,
                state = state,
                onSelect = {
                    viewModel.dispatch(SettingsEvent.SetTextSpeed(it))
                }
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            ItemName(
                name = strings.screenEffect,
                modifier = Modifier.padding(start = 40.dp)
            )

            Spacer(modifier = Modifier.width(56.dp))

            ScreenEffectRow(strings = strings)
        }

        Spacer(modifier = Modifier.height(18.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            ItemName(
                name = strings.language,
                modifier = Modifier.padding(start = 40.dp)
            )

            Spacer(modifier = Modifier.width(56.dp))

            LanguageRow(
                strings = strings,
                state = state,
                onChangeLanguage = {
                    viewModel.dispatch(SettingsEvent.ChangeLanguage(it))
                }
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            ItemName(
                name = strings.audioVolume,
                modifier = Modifier.padding(start = 40.dp)
            )

            Spacer(modifier = Modifier.width(56.dp))

            AudioVolumeRow(
                currentVolume = state.bgmVolume,
                onVolumeMinus = {
                    viewModel.dispatch(SettingsEvent.VolumeDown)
                },
                onVolumePlus = {
                    viewModel.dispatch(SettingsEvent.VolumeUp)
                }
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.width(132.dp))

            SaveButton(strings = strings)

            Spacer(modifier = Modifier.width(160.dp))

            LoadButton(strings = strings)
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
                    text = strings.backToGame,
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
                    text = strings.backToTitle,
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
                    text = strings.exitGame,
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
private fun TextSpeedRow(
    modifier: Modifier = Modifier,
    state: SettingsState,
    strings: BaseStrings,
    onSelect: (String) -> Unit
) {
    val slowSpeedMillis = remember {
        GameTypes.TextSpeed.getMillis(GameTypes.TextSpeed.Slow)
    }
    val normalSpeedMillis = remember {
        GameTypes.TextSpeed.getMillis(GameTypes.TextSpeed.Normal)
    }
    val fastSpeedMillis = remember {
        GameTypes.TextSpeed.getMillis(GameTypes.TextSpeed.Fast)
    }
    val fastestSpeedMillis = remember {
        GameTypes.TextSpeed.getMillis(GameTypes.TextSpeed.Fastest)
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PixelText(
            text = strings.speedSlow,
            fontSize = 20.sp,
            color = if (state.textSpeed == slowSpeedMillis) Color.White else Color.Black,
            modifier = Modifier.clickable {
                onSelect(GameTypes.TextSpeed.Slow)
            }
        )

        Spacer(modifier = Modifier.width(12.dp))
        DivideLine()
        Spacer(modifier = Modifier.width(12.dp))

        PixelText(
            text = strings.speedNormal,
            fontSize = 20.sp,
            color = if (state.textSpeed == normalSpeedMillis) Color.White else Color.Black,
            modifier = Modifier.clickable {
                onSelect(GameTypes.TextSpeed.Normal)
            }
        )

        Spacer(modifier = Modifier.width(12.dp))
        DivideLine()
        Spacer(modifier = Modifier.width(12.dp))

        PixelText(
            text = strings.speedFast,
            fontSize = 20.sp,
            color = if (state.textSpeed == fastSpeedMillis) Color.White else Color.Black,
            modifier = Modifier.clickable {
                onSelect(GameTypes.TextSpeed.Fast)
            }
        )

        Spacer(modifier = Modifier.width(12.dp))
        DivideLine()
        Spacer(modifier = Modifier.width(12.dp))

        PixelText(
            text = strings.speedFastest,
            fontSize = 20.sp,
            color = if (state.textSpeed == fastestSpeedMillis) Color.White else Color.Black,
            modifier = Modifier.clickable {
                onSelect(GameTypes.TextSpeed.Fastest)
            }
        )
    }
}

@Composable
private fun ScreenEffectRow(modifier: Modifier = Modifier, strings: BaseStrings) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PixelText(
            text = strings.on,
            fontSize = 20.sp,
            color = Color.White
        )

        Spacer(modifier = Modifier.width(12.dp))
        DivideLine()
        Spacer(modifier = Modifier.width(12.dp))

        PixelText(
            text = strings.off,
            fontSize = 20.sp
        )
    }
}

@Composable
private fun LanguageRow(
    modifier: Modifier = Modifier,
    strings: BaseStrings,
    state: SettingsState,
    onChangeLanguage: (Int) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PixelText(
            text = strings.languageJP,
            fontSize = 20.sp,
            color = if (state.language == Global.LANGUAGE_JP) Color.White else Color.Black,
            modifier = Modifier.clickable {
                onChangeLanguage(Global.LANGUAGE_JP)
            }
        )

        Spacer(modifier = Modifier.width(12.dp))
        DivideLine()
        Spacer(modifier = Modifier.width(12.dp))

        PixelText(
            text = strings.languageCN,
            fontSize = 20.sp,
            color = if (state.language == Global.LANGUAGE_CN) Color.White else Color.Black,
            modifier = Modifier.clickable {
                onChangeLanguage(Global.LANGUAGE_CN)
            }
        )
    }
}

@Composable
private fun AudioVolumeRow(
    modifier: Modifier = Modifier,
    currentVolume: Int,
    onVolumeMinus: () -> Unit,
    onVolumePlus: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        VolumeMinusIcon(onClick = onVolumeMinus)

        Spacer(modifier = Modifier.width(32.dp))

        PixelText(
            text = currentVolume.toString(),
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.width(32.dp))

        VolumePlusIcon(onClick = onVolumePlus)
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
private fun VolumeMinusIcon(onClick: () -> Unit) {
    Image(
        painter = painterResource("drawable/icon_triangle_left.png"),
        contentDescription = null,
        modifier = Modifier.clickable {
            onClick()
        }
    )
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun VolumePlusIcon(onClick: () -> Unit) {
    Image(
        painter = painterResource("drawable/icon_triangle_right.png"),
        contentDescription = null,
        modifier = Modifier.clickable {
            onClick()
        }
    )
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun SaveButton(strings: BaseStrings) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource("drawable/icon_save.png"),
            contentDescription = null
        )

        Spacer(modifier = Modifier.width(20.dp))

        PixelText(
            text = strings.save,
            fontSize = 20.sp
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun LoadButton(strings: BaseStrings) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource("drawable/icon_load.png"),
            contentDescription = null
        )

        Spacer(modifier = Modifier.width(20.dp))

        PixelText(
            text = strings.load,
            fontSize = 20.sp
        )
    }
}