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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rizz.focusbox.ui.theme.FocusBoxTheme
import com.rizz.focusbox.ui.theme.focusBoxDisplayTimerStyle

/**
 * The Figma "Progress ring" component defines a Theme x Mode (Idle/Focus/Paused/Short break/...)
 * variant set. Rather than bake every mode into this composable, it takes the state color as a
 * plain parameter - the caller (a future TimerViewModel) maps TimerEngine's SessionType/TimerState
 * to a color from FocusBoxTheme, matching this session's domain-first, UI-wiring-later pattern.
 */
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

@Preview
@Composable
private fun ProgressRingLightPreview() {
    FocusBoxTheme(darkTheme = false) {
        ProgressRing(
            progress = 0.65f,
            timeText = "14:59",
            ringColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview
@Composable
private fun ProgressRingDarkPreview() {
    FocusBoxTheme(darkTheme = true) {
        ProgressRing(
            progress = 0.65f,
            timeText = "14:59",
            ringColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(16.dp),
        )
    }
}
