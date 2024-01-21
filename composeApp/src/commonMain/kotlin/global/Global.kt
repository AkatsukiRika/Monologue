package global

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import createDataStore
import global.strings.BaseStrings
import global.strings.StringsCN
import global.strings.StringsJP
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

object Global {
    const val LANGUAGE_JP = 0
    const val LANGUAGE_CN = 1
    const val BGM_VOLUME_MAX = 10
    const val BGM_VOLUME_MIN = 0

    lateinit var Strings: BaseStrings
    val stringsFlow = MutableStateFlow<BaseStrings?>(null)

    val bgmVolumeFlow = MutableStateFlow(BGM_VOLUME_MAX)

    val textSpeedMillis: Long = 100L

    var dataStore: DataStore<Preferences>? = null

    var appPreferences: AppPreferences? = null

    fun initApp() {
        createDataStore()?.let { dataStore ->
            Global.dataStore = dataStore
            appPreferences = AppPreferences(dataStore)

            reloadAppPreferences()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun reloadAppPreferences() {
        GlobalScope.launch(Dispatchers.IO) {
            appPreferences?.getLanguage()?.let {
                setGlobalLanguage(language = it)
            }
            appPreferences?.getBGMVolume()?.let {
                bgmVolumeFlow.emit(it)
            }
        }
    }

    suspend fun setGlobalLanguage(language: Int) {
        Strings = when (language) {
            LANGUAGE_JP -> {
                StringsJP()
            }

            LANGUAGE_CN -> {
                StringsCN()
            }

            else -> {
                StringsJP()
            }
        }
        stringsFlow.emit(Strings)
    }
}