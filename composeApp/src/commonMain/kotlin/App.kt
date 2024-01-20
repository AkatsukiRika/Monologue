import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import global.Global
import global.Routes
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.rememberNavigator
import ui.scene.DebugScene
import ui.scene.GameScene
import ui.scene.SettingsScene
import ui.scene.StartScene

@Composable
fun App() {
    PreComposeApp {
        val navigator = rememberNavigator()
        NavHost(
            navigator = navigator,
            initialRoute = Routes.Start
        ) {
            scene(route = Routes.Start) {
                StartScene(navigator = navigator)
            }

            scene(route = Routes.Game) {
                GameScene(navigator = navigator)
            }

            scene(route = Routes.Settings) {
                SettingsScene(navigator = navigator)
            }

            scene(route = Routes.Debug) {
                DebugScene(navigator = navigator)
            }
        }
    }

    LaunchedEffect(Unit) {
        Global.initApp()
    }
}