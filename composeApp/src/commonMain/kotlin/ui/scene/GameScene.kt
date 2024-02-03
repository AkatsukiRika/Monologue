package ui.scene

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import getScenarioXML
import global.Global
import global.strings.StringsCN
import models.GameTypes
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.viewmodel.viewModel
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import ui.common.NormalText
import ui.common.ScaryText
import ui.common.SpecialRedText
import ui.common.SpecialWhiteText
import ui.common.SpeechLayout
import ui.screen.GameEffectScreen
import ui.screen.GameSettingsScreen
import ui.vm.GameEffect
import ui.vm.GameEvent
import ui.vm.GameViewModel
import utils.observeEffect
import kotlin.system.exitProcess

@OptIn(ExperimentalResourceApi::class)
@Composable
fun GameScene(navigator: Navigator) {
    val viewModel = viewModel(GameViewModel::class) {
        GameViewModel()
    }
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var effectType by remember { mutableStateOf("") }
    var isPlayingTextAnim by remember { mutableStateOf(false) }
    var skipTextAnim by remember { mutableIntStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {
                if (!isPlayingTextAnim) {
                    viewModel.dispatch(GameEvent.ClickNext)
                } else {
                    skipTextAnim++
                }
            }
    ) {
        val backImage = GameTypes.Back.getImage(state.currentBack)
        if (backImage != null) {
            Image(
                painter = painterResource(backImage),
                modifier = Modifier.fillMaxSize(),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }

        val frontImage = GameTypes.Front.getImage(state.currentFront)
        if (frontImage != null) {
            Image(
                painter = painterResource(frontImage),
                contentDescription = null,
                modifier = Modifier.align(Alignment.BottomCenter),
                alpha = state.currentFrontAlpha
            )
        }

        when {
            state.showSpecialWhiteText -> {
                SpecialWhiteText(
                    modifier = Modifier.align(Alignment.Center),
                    state = state
                )
            }

            state.showSpecialRedText -> {
                SpecialRedText(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 42.dp),
                    state = state
                )
            }

            state.showSpeechText -> {
                SpeechLayout(state = state)
            }

            state.showScaryText -> {
                ScaryText(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    textModifier = Modifier.padding(all = 12.dp),
                    lineHeight = 24.sp,
                    state = state,
                    onUpdateTextAnimStatus = { isPlayingTextAnim = it },
                    skipTextAnim = { skipTextAnim }
                )
            }

            state.showNormalText -> {
                NormalText(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    textModifier = Modifier.padding(all = 12.dp),
                    lineHeight = 24.sp,
                    state = state,
                    onUpdateTextAnimStatus = { isPlayingTextAnim = it },
                    skipTextAnim = { skipTextAnim }
                )
            }
        }

        if (state.showSettingsScreen) {
            GameSettingsScreen(
                onBackToMenu = {
                    navigator.goBack()
                },
                onEnd = {
                    exitProcess(status = 0)
                }
            )
        }

        SettingsIcon(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 16.dp, start = 16.dp)
                .size(32.dp)
                .clickable {
                    val showSettings = state.showSettingsScreen
                    viewModel.dispatch(GameEvent.ShowSettings(show = !showSettings))
                }
        )

        GameEffectScreen(
            state = state,
            type = effectType,
            onAfterEffect = {
                effectType = ""
                viewModel.dispatch(GameEvent.ClickNext)
            }
        )
    }

    LaunchedEffect(Unit) {
        val fileName = if (Global.Strings is StringsCN) "game_scenario_cn.xml" else "game_scenario_jp.xml"
        val xmlText = getScenarioXML(fileName)
        viewModel.dispatch(GameEvent.LoadScenario(xmlText))
    }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.observeEffect {
            if (it is GameEffect.ShowEffect) {
                effectType = it.effect
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun SettingsIcon(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource("drawable/icon_game_settings.png"),
        contentDescription = null,
        modifier = modifier
    )
}