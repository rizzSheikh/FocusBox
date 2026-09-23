package com.rizz.focusbox.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.rizz.focusbox.ui.theme.FocusBoxTheme
import com.rizz.focusbox.ui.theme.PreviewLightDark

enum class FocusBoxDestination(val label: String, val icon: ImageVector) {
    Timer("Timer", Icons.Filled.Timer),
    Stats("Stats", Icons.Filled.BarChart),
    Settings("Settings", Icons.Filled.Settings),
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
                icon = { Icon(imageVector = destination.icon, contentDescription = null) },
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
