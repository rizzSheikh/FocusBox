package com.rizz.focusbox.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rizz.focusbox.ui.theme.FocusBoxTheme

/** A generic settings list row: label (+ optional subtitle) on the left, a trailing control on the right. */
@Composable
fun SettingsRow(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    trailing: @Composable () -> Unit = {},
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge)
            if (subtitle != null) {
                Text(text = subtitle, style = MaterialTheme.typography.bodyMedium)
            }
        }
        trailing()
    }
}

@Preview
@Composable
private fun SettingsRowLightPreview() {
    FocusBoxTheme(darkTheme = false) {
        SettingsRow(
            title = "Notifications",
            subtitle = "Show a notification while focusing",
            modifier = Modifier.padding(16.dp),
            trailing = { FocusBoxSwitch(checked = true, onCheckedChange = {}) },
        )
    }
}

@Preview
@Composable
private fun SettingsRowDarkPreview() {
    FocusBoxTheme(darkTheme = true) {
        SettingsRow(
            title = "Theme",
            subtitle = "System",
            modifier = Modifier.padding(16.dp),
        )
    }
}
