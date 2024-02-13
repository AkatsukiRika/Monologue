import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import global.Routes
import javafx.application.Platform
import javafx.embed.swing.JFXPanel
import javafx.scene.Scene
import javafx.scene.layout.StackPane
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.scene.media.MediaView
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import java.awt.Dimension
import javax.swing.SwingUtilities

private const val VIDEO_FILE_NAME = "bad_end.mp4"

@Composable
fun VideoSceneDesktop(navigator: Navigator) {
    var jfxPanel by remember { mutableStateOf<JFXPanel?>(null) }
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    LaunchedEffect(Unit) {
        SwingUtilities.invokeLater {
            jfxPanel = JFXPanel()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .onGloballyPositioned {
                SwingUtilities.invokeLater {
                    jfxPanel?.preferredSize = Dimension(it.size.width, it.size.height)
                }
            }
    ) {
        jfxPanel?.let {
            SwingPanel(
                modifier = Modifier.fillMaxSize(),
                factory = { it }
            )
        }

        DisposableEffect(jfxPanel) {
            if (jfxPanel != null) {
                Platform.runLater {
                    val url = DesktopUtils.getResourceURL(VIDEO_FILE_NAME) ?: return@runLater
                    val tempFile = DesktopUtils.createTempFileFromResource(url, ".mp4")
                    val osName = System.getProperty("os.name")
                    val videoSource = if (osName.lowercase().contains("windows")) {
                        "file:///${tempFile.absolutePath.replace("\\", "/")}"
                    } else {
                        "file://${tempFile.absolutePath}"
                    }
                    val media = Media(videoSource)
                    mediaPlayer = MediaPlayer(media)
                    val mediaView = MediaView(mediaPlayer)

                    val root = StackPane()
                    root.children.add(mediaView)
                    root.style = "-fx-background-color: BLACK;"
                    val scene = Scene(root)
                    jfxPanel?.scene = scene
                    mediaPlayer?.play()
                    mediaPlayer?.setOnEndOfMedia {
                        navigator.navigate(
                            route = Routes.Start,
                            options = NavOptions(launchSingleTop = true)
                        )
                    }
                }
            }

            onDispose {
                mediaPlayer?.stop()
                mediaPlayer?.dispose()
                mediaPlayer = null
            }
        }
    }
}