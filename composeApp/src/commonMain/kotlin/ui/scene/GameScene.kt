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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.viewmodel.viewModel
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import ui.vm.GameEvent
import ui.vm.GameViewModel

@Composable
fun GameScene(navigator: Navigator) {
    val viewModel = viewModel(GameViewModel::class) {
        GameViewModel()
    }
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {
                viewModel.dispatch(GameEvent.ClickNext)
            }
    ) {
        SettingsIcon(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 16.dp, start = 16.dp)
                .size(32.dp)
                .clickable {
                    val showSettings = state.showSettingsSubScreen
                    viewModel.dispatch(GameEvent.ShowSettings(show = !showSettings))
                }
        )
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