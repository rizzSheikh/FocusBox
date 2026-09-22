package com.rizz.focusbox.ui.components

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.rizz.focusbox.ui.theme.FocusBoxTheme

enum class FocusBoxDestination(val label: String) {
    Timer("Timer"),
    Stats("Stats"),
    Settings("Settings"),
}

/**
 * Fixed to the app's 3 destinations, matching the Figma "Navigation bar" component - not a
 * generic N-item nav bar. Labels only (no icon set is bundled yet in this pass); the label
 * style matches the Figma "Label small - 12/Medium" token already wired into FocusBoxTypography.
 */
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

@Preview
@Composable
private fun FocusBoxNavigationBarLightPreview() {
    FocusBoxTheme(darkTheme = false) {
        FocusBoxNavigationBar(selected = FocusBoxDestination.Timer, onSelect = {})
    }
}

@Preview
@Composable
private fun FocusBoxNavigationBarDarkPreview() {
    FocusBoxTheme(darkTheme = true) {
        FocusBoxNavigationBar(selected = FocusBoxDestination.Timer, onSelect = {})
    }
}
