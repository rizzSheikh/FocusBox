package com.rizz.focusbox.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rizz.focusbox.ui.theme.FocusBoxTheme

@Composable
fun FocusBoxSnackbar(
    message: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: () -> Unit = {},
) {
    Snackbar(
        modifier = modifier,
        action = actionLabel?.let {
            { TextButton(onClick = onAction) { Text(it) } }
        },
    ) {
        Text(message)
    }
}

@Preview
@Composable
private fun FocusBoxSnackbarLightPreview() {
    FocusBoxTheme(darkTheme = false) {
        FocusBoxSnackbar(
            message = "Session skipped",
            actionLabel = "Undo",
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview
@Composable
private fun FocusBoxSnackbarDarkPreview() {
    FocusBoxTheme(darkTheme = true) {
        FocusBoxSnackbar(
            message = "Session skipped",
            actionLabel = "Undo",
            modifier = Modifier.padding(16.dp),
        )
    }
}
