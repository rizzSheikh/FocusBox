package com.rizz.focusbox.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rizz.focusbox.ui.theme.FocusBoxTheme

@Composable
fun FocusBoxTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier,
        singleLine = true,
    )
}

@Preview
@Composable
private fun FocusBoxTextFieldLightPreview() {
    FocusBoxTheme(darkTheme = false) {
        FocusBoxTextField(
            value = "Write report",
            onValueChange = {},
            label = "Task",
            modifier = Modifier.fillMaxWidth().padding(16.dp),
        )
    }
}

@Preview
@Composable
private fun FocusBoxTextFieldDarkPreview() {
    FocusBoxTheme(darkTheme = true) {
        FocusBoxTextField(
            value = "Write report",
            onValueChange = {},
            label = "Task",
            modifier = Modifier.fillMaxWidth().padding(16.dp),
        )
    }
}
