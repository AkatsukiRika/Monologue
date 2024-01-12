import androidx.compose.runtime.Composable
import global.Routes
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.rememberNavigator
import ui.GameScene
import ui.StartScene

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
        }
    }
}