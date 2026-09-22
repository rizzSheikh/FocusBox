package com.rizz.focusbox.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rizz.focusbox.ui.theme.FocusBoxTheme

@Composable
fun FocusBoxButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(onClick = onClick, modifier = modifier, enabled = enabled) {
        Text(text)
    }
}

@Preview
@Composable
private fun FocusBoxButtonLightPreview() {
    FocusBoxTheme(darkTheme = false) {
        FocusBoxButton(text = "Start focus", onClick = {}, modifier = Modifier.padding(16.dp))
    }
}

@Preview
@Composable
private fun FocusBoxButtonDarkPreview() {
    FocusBoxTheme(darkTheme = true) {
        FocusBoxButton(text = "Start focus", onClick = {}, modifier = Modifier.padding(16.dp))
    }
}
