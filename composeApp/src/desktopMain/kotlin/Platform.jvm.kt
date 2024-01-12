import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import models.GameModels
import org.w3c.dom.Element
import org.xml.sax.InputSource
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory

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
            val childNodes = scene.childNodes
            val sceneElements = mutableListOf<GameModels.SceneElement>()

            for (j in 0 until childNodes.length) {
                val childNode = childNodes.item(j)
                (childNode as? Element)?.let { element ->
                    if (element.tagName == "text") {
                        val type = element.getAttribute("type")
                        val character = element.getAttribute("character")
                        val time = element.getAttribute("time")
                        val content = element.textContent
                        val textObject = GameModels.Text(
                            type = type,
                            character = character,
                            time = time,
                            content = content
                        )
                        sceneElements.add(textObject)
                    } else if (element.tagName == "effect") {
                        val type = element.getAttribute("type")
                        val effectFront = element.getAttribute("front")
                        val image = element.getAttribute("image")
                        val effectObject = GameModels.Effect(
                            type = type,
                            front = effectFront,
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