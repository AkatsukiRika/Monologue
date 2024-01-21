import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import global.Global
import javafx.application.Platform
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import models.GameModels
import org.w3c.dom.Element
import org.xml.sax.InputSource
import java.io.File
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.math.min

@Composable
actual fun getFont(
    name: String,
    res: String,
    weight: FontWeight,
    style: FontStyle
): Font = androidx.compose.ui.text.platform.Font("font/$res.ttf", weight, style)

actual fun getScenarioXML(fileName: String): String {
    try {
        val url = DesktopUtils.getResourceURL(fileName) ?: return ""
        val tempFile = DesktopUtils.createTempFileFromResource(url)
        return tempFile.readText(charset = Charsets.UTF_8)
    } catch (e: Exception) {
        e.printStackTrace()
        return ""
    }
}

actual fun parseScenarioXML(data: String): GameModels.Scenario {
    val builderFactory = DocumentBuilderFactory.newInstance()
    val docBuilder = builderFactory.newDocumentBuilder()
    val inputSource = InputSource(StringReader(data))
    val document = docBuilder.parse(inputSource)

    try {
        val scenario = document.getElementsByTagName("scenario").item(0) as Element
        val language = scenario.getAttribute("language")
        val scenes = scenario.getElementsByTagName("scene")
        val sceneObjects = mutableListOf<GameModels.Scene>()

        for (i in 0 until scenes.length) {
            val scene = scenes.item(i) as Element
            val id = scene.getAttribute("id")
            val back = scene.getAttribute("back")
            val front = scene.getAttribute("front")
            val frontAlpha = scene.getAttribute("frontAlpha").toFloatOrNull() ?: 1f
            val childNodes = scene.childNodes
            val sceneElements = mutableListOf<GameModels.SceneElement>()

            for (j in 0 until childNodes.length) {
                val childNode = childNodes.item(j)
                (childNode as? Element)?.let { element ->
                    if (element.tagName == "text") {
                        val type = element.getAttribute("type")
                        val character = element.getAttribute("character")
                        val time = element.getAttribute("time")
                        val voice = element.getAttribute("voice")
                        val content = element.textContent
                        val textObject = GameModels.Text(
                            type = type,
                            character = character,
                            time = time,
                            content = content,
                            voice = voice
                        )
                        sceneElements.add(textObject)
                    } else if (element.tagName == "effect") {
                        val type = element.getAttribute("type")
                        val effectFront = element.getAttribute("front")
                        val effectFrontAlpha = element.getAttribute("frontAlpha").toFloatOrNull()
                        val image = element.getAttribute("image")
                        val effectObject = GameModels.Effect(
                            type = type,
                            front = effectFront,
                            frontAlpha = effectFrontAlpha,
                            image = image
                        )
                        sceneElements.add(effectObject)
                    } else {
                        // no-op
                    }
                }
            }

            val sceneObject = GameModels.Scene(
                id = id.toIntOrNull() ?: 0,
                back = back,
                front = front,
                frontAlpha = frontAlpha,
                elements = sceneElements
            )
            sceneObjects.add(sceneObject)
        }

        return GameModels.Scenario(
            language = language,
            scenes = sceneObjects
        )
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return GameModels.Scenario(language = "none")
}

private var isJavaFXPlatformInit = false

private var mediaPlayer: MediaPlayer? = null

private var voicePlayer: MediaPlayer? = null

@OptIn(DelicateCoroutinesApi::class)
actual fun playAudioFile(fileName: String, loop: Boolean) {
    if (!isJavaFXPlatformInit) {
        Platform.startup {}
        isJavaFXPlatformInit = true
    }

    Platform.runLater {
        val url = DesktopUtils.getResourceURL(fileName) ?: return@runLater
        val tempFile = DesktopUtils.createTempFileFromResource(url, extension = ".mp3")
        val osName = System.getProperty("os.name")
        val audioSource = if (osName.lowercase().contains("windows")) {
            "file:///${tempFile.absolutePath.replace("\\", "/")}"
        } else {
            "file://${tempFile.absolutePath}"
        }
        GlobalScope.launch(Dispatchers.Main) {
            val media = Media(audioSource)
            mediaPlayer?.stop()
            mediaPlayer = MediaPlayer(media)
            val volume = Global.bgmVolumeFlow.first()
            mediaPlayer?.volume = volume.toDouble() / Global.BGM_VOLUME_MAX
            if (loop) {
                mediaPlayer?.cycleCount = MediaPlayer.INDEFINITE
            }
            mediaPlayer?.play()
        }
    }
}

actual fun stopAudio() {
    mediaPlayer?.stop()
}

@OptIn(DelicateCoroutinesApi::class)
actual fun playVoice(fileName: String) {
    if (!isJavaFXPlatformInit) {
        throw Exception("JavaFX Platform未初始化!")
    }

    Platform.runLater {
        val url = DesktopUtils.getResourceURL(fileName) ?: return@runLater
        val tempFile = DesktopUtils.createTempFileFromResource(url, extension = ".wav")
        val osName = System.getProperty("os.name")
        val audioSource = if (osName.lowercase().contains("windows")) {
            "file:///${tempFile.absolutePath.replace("\\", "/")}"
        } else {
            "file://${tempFile.absolutePath}"
        }
        GlobalScope.launch(Dispatchers.Main) {
            val media = Media(audioSource)
            voicePlayer?.stop()
            voicePlayer = MediaPlayer(media)
            voicePlayer?.play()
            val bgmVolume = Global.bgmVolumeFlow.first().toDouble() / Global.BGM_VOLUME_MAX
            mediaPlayer?.volume = min(bgmVolume, 0.25)
            voicePlayer?.setOnEndOfMedia {
                mediaPlayer?.volume = bgmVolume
            }
        }
    }
}

actual fun setAudioVolume(volume: Float) {
    if (!isJavaFXPlatformInit) {
        throw Exception("JavaFX Platform未初始化!")
    }

    Platform.runLater {
        mediaPlayer?.volume = volume.toDouble()
    }
}

actual fun createDataStore(): DataStore<Preferences>? = createDataStoreWithDefaults {
    DesktopUtils.getHomeDirectory() + File.separator + "test.preferences_pb"
}