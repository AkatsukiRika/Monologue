package ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import getFont

object AppFonts {
    @Composable
    fun MSGothic() = FontFamily(
        getFont(
            name = "MS Gothic",
            res = "ms_gothic",
            FontWeight.Normal,
            FontStyle.Normal
        )
    )

    @Composable
    fun ZPix() = FontFamily(
        getFont(
            name = "ZPix",
            res = "zpix",
            FontWeight.Normal,
            FontStyle.Normal
        )
    )
}