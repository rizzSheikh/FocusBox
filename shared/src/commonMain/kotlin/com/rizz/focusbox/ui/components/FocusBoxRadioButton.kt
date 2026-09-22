package com.rizz.focusbox.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rizz.focusbox.ui.theme.FocusBoxTheme

@Composable
fun FocusBoxRadioButton(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    RadioButton(selected = selected, onClick = onClick, modifier = modifier)
}

@Preview
@Composable
private fun FocusBoxRadioButtonLightPreview() {
    FocusBoxTheme(darkTheme = false) {
        FocusBoxRadioButton(selected = true, onClick = {}, modifier = Modifier.padding(16.dp))
    }
}

@Preview
@Composable
private fun FocusBoxRadioButtonDarkPreview() {
    FocusBoxTheme(darkTheme = true) {
        FocusBoxRadioButton(selected = true, onClick = {}, modifier = Modifier.padding(16.dp))
    }
}
