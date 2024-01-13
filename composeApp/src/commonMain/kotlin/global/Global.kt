package global

import global.strings.BaseStrings
import global.strings.StringsJP

object Global {
    val Strings: BaseStrings = StringsJP()

    val textSpeedMillis: Long = 100L
}