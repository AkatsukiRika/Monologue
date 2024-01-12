package utils

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

fun Modifier.sideBorders(
    colorTop: Color, colorBottom: Color, colorStart: Color, colorEnd: Color, strokeWidth: Dp
) = this.drawBehind {
    val strokeWidthPx = strokeWidth.toPx()

    // 顶部边框
    drawLine(
        color = colorTop,
        start = Offset(0f, 0f),
        end = Offset(size.width, 0f),
        strokeWidth = strokeWidthPx
    )

    // 底部边框
    drawLine(
        color = colorBottom,
        start = Offset(0f, size.height),
        end = Offset(size.width, size.height),
        strokeWidth = strokeWidthPx
    )

    // 左侧边框
    drawLine(
        color = colorStart,
        start = Offset(0f, 0f),
        end = Offset(0f, size.height),
        strokeWidth = strokeWidthPx
    )

    // 右侧边框
    drawLine(
        color = colorEnd,
        start = Offset(size.width, 0f),
        end = Offset(size.width, size.height),
        strokeWidth = strokeWidthPx
    )
}