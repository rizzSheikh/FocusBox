package com.rizz.focusbox.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rizz.focusbox.ui.theme.FocusBoxTheme

@Composable
fun FocusBoxSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Switch(checked = checked, onCheckedChange = onCheckedChange, modifier = modifier)
}

@Preview
@Composable
private fun FocusBoxSwitchLightPreview() {
    FocusBoxTheme(darkTheme = false) {
        FocusBoxSwitch(checked = true, onCheckedChange = {}, modifier = Modifier.padding(16.dp))
    }
}

@Preview
@Composable
private fun FocusBoxSwitchDarkPreview() {
    FocusBoxTheme(darkTheme = true) {
        FocusBoxSwitch(checked = true, onCheckedChange = {}, modifier = Modifier.padding(16.dp))
    }
}
