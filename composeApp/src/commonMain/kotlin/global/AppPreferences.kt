package global

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import models.GameTypes
import setAudioVolume

class AppPreferences(private val dataStore: DataStore<Preferences>) {
    companion object {
        const val KEY_TEST_STRING = "test_string"
        const val KEY_LANGUAGE = "language"
        const val KEY_BGM_VOLUME = "bgm_volume"
        const val KEY_TEXT_SPEED = "text_speed"
    }

    private val testStringKey = stringPreferencesKey(KEY_TEST_STRING)

    private val languageKey = intPreferencesKey(KEY_LANGUAGE)

    private val bgmVolumeKey = intPreferencesKey(KEY_BGM_VOLUME)

    private val textSpeedKey = longPreferencesKey(KEY_TEXT_SPEED)

    suspend fun getTestString() = dataStore.data.map { preferences ->
        preferences[testStringKey] ?: ""
    }.first()

    suspend fun setTestString(value: String) = dataStore.edit { preferences ->
        preferences[testStringKey] = value
    }

    suspend fun getLanguage() = dataStore.data.map { preferences ->
        preferences[languageKey] ?: Global.LANGUAGE_JP
    }.first()

    suspend fun setLanguage(value: Int) = dataStore.edit { preferences ->
        Global.setGlobalLanguage(language = value)
        preferences[languageKey] = value
    }

    suspend fun getBGMVolume() = dataStore.data.map { preferences ->
        preferences[bgmVolumeKey] ?: Global.BGM_VOLUME_MAX
    }.first()

    suspend fun setBGMVolume(value: Int) = dataStore.edit { preferences ->
        if (value in Global.BGM_VOLUME_MIN..Global.BGM_VOLUME_MAX) {
            val floatVolume = value.toFloat() / Global.BGM_VOLUME_MAX
            setAudioVolume(volume = floatVolume)
            preferences[bgmVolumeKey] = value
        }
    }

    suspend fun getTextSpeed() = dataStore.data.map { preferences ->
        preferences[textSpeedKey] ?: GameTypes.TextSpeed.getMillis(GameTypes.TextSpeed.Normal)
    }.first()

    suspend fun setTextSpeed(value: Long) = dataStore.edit { preferences ->
        Global.textSpeedMillis = value
        preferences[textSpeedKey] = value
    }
}