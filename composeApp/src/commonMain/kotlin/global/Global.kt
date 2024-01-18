package global

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import global.strings.BaseStrings
import global.strings.StringsJP

object Global {
    val Strings: BaseStrings = StringsJP()

    val textSpeedMillis: Long = 100L

    var dataStore: DataStore<Preferences>? = null

    var appPreferences: AppPreferences? = null
}