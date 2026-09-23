package com.rizz.focusbox.ui.components

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rizz.focusbox.ui.theme.FocusBoxTheme
import com.rizz.focusbox.ui.theme.PreviewLightDark

enum class FocusBoxDestination(val label: String) {
    Timer("Timer"),
    Stats("Stats"),
    Settings("Settings"),
}

@Composable
fun FocusBoxNavigationBar(
    selected: FocusBoxDestination,
    onSelect: (FocusBoxDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(modifier = modifier) {
        FocusBoxDestination.entries.forEach { destination ->
            NavigationBarItem(
                selected = destination == selected,
                onClick = { onSelect(destination) },
                icon = {},
                label = { Text(destination.label) },
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun FocusBoxNavigationBarPreview() {
    FocusBoxTheme {
        FocusBoxNavigationBar(selected = FocusBoxDestination.Timer, onSelect = {})
    }
}
