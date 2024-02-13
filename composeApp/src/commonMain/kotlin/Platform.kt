import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import models.GameModels
import okio.Path.Companion.toPath

@Composable
expect fun getFont(name: String, res: String, weight: FontWeight, style: FontStyle): Font

expect fun getScenarioXML(fileName: String): String

expect fun parseScenarioXML(data: String): GameModels.Scenario

expect fun playAudioFile(fileName: String, loop: Boolean = false)

expect fun stopAudio()

expect fun playVoice(fileName: String)

expect fun setAudioVolume(volume: Float)

/**
 * KMP DataStore使用参考文档：https://funkymuse.dev/posts/create-data-store-kmp/
 */
internal fun createDataStoreWithDefaults(
    corruptionHandler: ReplaceFileCorruptionHandler<Preferences>? = null,
    coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
    migrations: List<DataMigration<Preferences>> = emptyList(),
    path: () -> String
) = PreferenceDataStoreFactory.createWithPath(
    corruptionHandler = corruptionHandler,
    scope = coroutineScope,
    migrations = migrations,
    produceFile = {
        path().toPath()
    }
)

expect fun createDataStore(): DataStore<Preferences>?

@Composable
expect fun VideoScene()