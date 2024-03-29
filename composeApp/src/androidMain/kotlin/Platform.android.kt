import android.annotation.SuppressLint
import android.media.MediaPlayer
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.tangping.monologue.VideoSceneAndroid
import global.Global
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import models.GameModels
import moe.tlaster.precompose.navigation.Navigator
import org.w3c.dom.Element
import org.xml.sax.InputSource
import java.io.File
import java.io.StringReader
import java.nio.charset.StandardCharsets
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.math.min

@SuppressLint("DiscouragedApi")
@Composable
actual fun getFont(name: String, res: String, weight: FontWeight, style: FontStyle): Font {
    val context = LocalContext.current
    val id = context.resources.getIdentifier(res, "font", context.packageName)
    return Font(id, weight, style)
}

@SuppressLint("DiscouragedApi")
actual fun getScenarioXML(fileName: String): String {
    // 去除后缀名
    val context = GlobalData.context ?: return ""
    val split = fileName.split(".")
    if (split.isEmpty()) {
        return ""
    }
    val path = split[0]

    val resourceId = context.resources.getIdentifier(path, "raw", context.packageName)
    val inputStream = context.resources.openRawResource(resourceId)
    val reader = inputStream.reader(charset = StandardCharsets.UTF_8)
    return reader.readText()
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
                    } else if (element.tagName == "music") {
                        val type = element.getAttribute("type")
                        val file = element.getAttribute("file")
                        val musicObject = GameModels.Music(
                            type = type,
                            file = file
                        )
                        sceneElements.add(musicObject)
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

        val ending = scenario.getElementsByTagName("ending")
        (ending.item(0) as? Element)?.let { element ->
            val type = element.getAttribute("type")
            val endingObject = GameModels.Ending(type = type)
            sceneObjects.add(endingObject)
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

private var mediaPlayer: MediaPlayer? = null

private var voicePlayer: MediaPlayer? = null

@OptIn(DelicateCoroutinesApi::class)
@SuppressLint("DiscouragedApi")
actual fun playAudioFile(fileName: String, loop: Boolean) {
    // 去除后缀名
    val context = GlobalData.context ?: return
    val split = fileName.split(".")
    if (split.isEmpty()) {
        return
    }
    val path = split[0]

    GlobalScope.launch(Dispatchers.Main) {
        val resourceId = context.resources.getIdentifier(path, "raw", context.packageName)
        mediaPlayer?.stop()
        mediaPlayer = MediaPlayer.create(context, resourceId)
        val bgmVolume = Global.bgmVolumeFlow.first().toFloat() / Global.BGM_VOLUME_MAX
        mediaPlayer?.setVolume(bgmVolume, bgmVolume)
        mediaPlayer?.isLooping = loop
        mediaPlayer?.start()
    }
}

actual fun stopAudio() {
    mediaPlayer?.stop()
}

@OptIn(DelicateCoroutinesApi::class)
@SuppressLint("DiscouragedApi")
actual fun playVoice(fileName: String) {
    // 去除后缀名
    val context = GlobalData.context ?: return
    val split = fileName.split(".")
    if (split.isEmpty()) {
        return
    }
    val path = split[0]

    GlobalScope.launch(Dispatchers.Main) {
        val resourceId = context.resources.getIdentifier(path, "raw", context.packageName)
        voicePlayer?.stop()
        voicePlayer = MediaPlayer.create(context, resourceId)
        voicePlayer?.start()
        val bgmVolume = Global.bgmVolumeFlow.first().toFloat() / Global.BGM_VOLUME_MAX
        mediaPlayer?.setVolume(min(bgmVolume, 0.25f), min(bgmVolume, 0.25f))
        voicePlayer?.setOnCompletionListener {
            mediaPlayer?.setVolume(bgmVolume, bgmVolume)
        }
    }
}

actual fun setAudioVolume(volume: Float) {
    mediaPlayer?.setVolume(volume, volume)
}

actual fun createDataStore(): DataStore<Preferences>? {
    val context = GlobalData.context ?: return null
    return createDataStoreWithDefaults {
        File(context.applicationContext.filesDir, "monologue.preferences_pb").path
    }
}

@Composable
actual fun VideoScene(navigator: Navigator) {
    VideoSceneAndroid()
}