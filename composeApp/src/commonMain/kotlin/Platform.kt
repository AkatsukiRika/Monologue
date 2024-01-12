import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import models.GameModels

@Composable
expect fun getFont(name: String, res: String, weight: FontWeight, style: FontStyle): Font

expect fun getScenarioXML(fileName: String): String

expect fun parseScenarioXML(data: String): GameModels.Scenario