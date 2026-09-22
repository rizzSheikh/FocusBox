package com.rizz.focusbox.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rizz.focusbox.ui.theme.FocusBoxTheme

/**
 * One row of the Session history list. A completed focus session shows a checkmark glyph; a
 * break or a skipped session shows a plain colored dot (skipped uses a muted/gray indicatorColor,
 * matching the Figma history screen where a skipped entry shows its actual elapsed duration,
 * not the planned one - callers read that straight off TimerEngine's FocusSessionRecord).
 */
@Composable
fun SessionItem(
    title: String,
    subtitle: String,
    duration: String,
    indicatorColor: Color,
    completed: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        StatusIndicator(color = indicatorColor, showCheck = completed)
        Column(modifier = Modifier.weight(1f).padding(horizontal = 12.dp)) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge)
            Text(text = subtitle, style = MaterialTheme.typography.bodyMedium)
        }
        Text(text = duration, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun StatusIndicator(color: Color, showCheck: Boolean) {
    Row(
        modifier = Modifier.size(20.dp).background(color, CircleShape),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (showCheck) {
            Text(text = "✓", color = Color.White, style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Preview
@Composable
private fun SessionItemLightPreview() {
    FocusBoxTheme(darkTheme = false) {
        SessionItem(
            title = "Design onboarding flow",
            subtitle = "Focus · 10:05 AM",
            duration = "25 min",
            indicatorColor = MaterialTheme.colorScheme.primary,
            completed = true,
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview
@Composable
private fun SessionItemDarkPreview() {
    FocusBoxTheme(darkTheme = true) {
        SessionItem(
            title = "Write API docs",
            subtitle = "Skipped · 9:10 AM",
            duration = "12 min",
            indicatorColor = MaterialTheme.colorScheme.outline,
            completed = false,
            modifier = Modifier.padding(16.dp),
        )
    }
}
