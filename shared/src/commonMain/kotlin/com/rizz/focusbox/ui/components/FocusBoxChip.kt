package com.rizz.focusbox.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rizz.focusbox.ui.theme.FocusBoxTheme

@Composable
fun FocusBoxChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(text) },
        modifier = modifier,
    )
}

@Preview
@Composable
private fun FocusBoxChipLightPreview() {
    FocusBoxTheme(darkTheme = false) {
        FocusBoxChip(text = "Focus", selected = true, onClick = {}, modifier = Modifier.padding(16.dp))
    }
}

@Preview
@Composable
private fun FocusBoxChipDarkPreview() {
    FocusBoxTheme(darkTheme = true) {
        FocusBoxChip(text = "Focus", selected = true, onClick = {}, modifier = Modifier.padding(16.dp))
    }
}
