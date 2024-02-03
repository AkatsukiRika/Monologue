package ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import utils.sideBorders

@Composable
fun PixelText(
    text: String,
    fontSize: TextUnit = 16.sp,
    color: Color = Color.Black,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit? = null
) {
    Text(
        text = text,
        fontFamily = AppFonts.ZPix(),
        fontSize = fontSize,
        color = color,
        modifier = modifier,
        textAlign = textAlign,
        lineHeight = lineHeight ?: fontSize
    )
}

@Composable
fun CommonButton(
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 1.dp,
    enabled: Boolean = true,
    onBoundingBoxChanged: ((Rect) -> Unit)? = null,
    onClick: () -> Unit,
    content: @Composable (RowScope) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val colorTopAndStart = if (isPressed) Color.Black else Color.White
    val colorBottomAndEnd = if (isPressed) Color.White else Color.Black

    Box(
        modifier = modifier
            .sideBorders(
                colorTop = colorTopAndStart,
                colorStart = colorTopAndStart,
                colorBottom = colorBottomAndEnd,
                colorEnd = colorBottomAndEnd,
                strokeWidth = strokeWidth
            )
            .onGloballyPositioned {
                val globalPosition = it.localToWindow(Offset.Zero)
                val boundingBox = Rect(
                    left = globalPosition.x,
                    top = globalPosition.y,
                    right = globalPosition.x + it.size.width,
                    bottom = globalPosition.y + it.size.height
                )
                onBoundingBoxChanged?.invoke(boundingBox)
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
                .wrapContentWidth()
                .clickable(interactionSource = interactionSource, indication = null, enabled = enabled) {
                    onClick()
                }
        ) {
            content(this)
        }
    }
}