package global

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class AppPreferences(private val dataStore: DataStore<Preferences>) {
    companion object {
        const val KEY_TEST_STRING = "test_string"
        const val KEY_LANGUAGE = "language"
    }

    private val testStringKey = stringPreferencesKey(KEY_TEST_STRING)

    private val languageKey = intPreferencesKey(KEY_LANGUAGE)

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
}