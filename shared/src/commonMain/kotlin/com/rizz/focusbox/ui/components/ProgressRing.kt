package com.rizz.focusbox.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.rizz.focusbox.ui.theme.FocusBoxTheme
import com.rizz.focusbox.ui.theme.PreviewLightDark
import com.rizz.focusbox.ui.theme.focusBoxDisplayTimerStyle

@Composable
fun ProgressRing(
    progress: Float,
    timeText: String,
    ringColor: Color,
    modifier: Modifier = Modifier,
    trackColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    strokeWidth: androidx.compose.ui.unit.Dp = 8.dp,
) {
    Box(modifier = modifier.size(220.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(220.dp)) {
            val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            drawArc(
                color = trackColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = stroke,
            )
            drawArc(
                color = ringColor,
                startAngle = -90f,
                sweepAngle = 360f * progress.coerceIn(0f, 1f),
                useCenter = false,
                style = stroke,
            )
        }
        Text(text = timeText, style = focusBoxDisplayTimerStyle())
    }
}

@PreviewLightDark
@Composable
private fun ProgressRingPreview() {
    FocusBoxTheme {
        ProgressRing(
            progress = 0.65f,
            timeText = "14:59",
            ringColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(16.dp),
        )
    }
}
