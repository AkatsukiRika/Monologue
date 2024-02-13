import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Monologue",
        state = WindowState(size = DpSize(960.dp, 720.dp))
    ) {
        App()
    }
}

@Preview
@Composable
fun AppDesktopPreview() {
    App()
}