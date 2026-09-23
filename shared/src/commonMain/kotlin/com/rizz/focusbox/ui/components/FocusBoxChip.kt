package com.rizz.focusbox.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rizz.focusbox.ui.theme.FocusBoxTheme
import com.rizz.focusbox.ui.theme.PreviewLightDark

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

@PreviewLightDark
@Composable
private fun FocusBoxChipPreview() {
    FocusBoxTheme {
        FocusBoxChip(text = "Focus", selected = true, onClick = {}, modifier = Modifier.padding(16.dp))
    }
}
