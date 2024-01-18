package global

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class AppPreferences(private val dataStore: DataStore<Preferences>) {
    companion object {
        const val KEY_TEST_STRING = "test_string"
    }

    private val testStringKey = stringPreferencesKey(KEY_TEST_STRING)

    suspend fun getTestString() = dataStore.data.map { preferences ->
        preferences[testStringKey] ?: ""
    }.first()

    suspend fun setTestString(value: String) = dataStore.edit { preferences ->
        preferences[testStringKey] = value
    }
}