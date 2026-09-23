package com.rizz.focusbox.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rizz.focusbox.ui.theme.FocusBoxTheme
import com.rizz.focusbox.ui.theme.PreviewLightDark

@Composable
fun FocusBoxRadioButton(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    RadioButton(selected = selected, onClick = onClick, modifier = modifier)
}

@PreviewLightDark
@Composable
private fun FocusBoxRadioButtonPreview() {
    FocusBoxTheme {
        FocusBoxRadioButton(selected = true, onClick = {}, modifier = Modifier.padding(16.dp))
    }
}
