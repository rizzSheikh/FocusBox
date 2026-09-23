package com.rizz.focusbox.feature.home.view.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rizz.focusbox.ui.theme.FocusBoxTheme
import com.rizz.focusbox.ui.theme.PreviewLightDark
import focusbox.shared.generated.resources.Res
import focusbox.shared.generated.resources.home_placeholder
import org.jetbrains.compose.resources.stringResource

/**
 * Placeholder landing screen — onboarding routes here once complete. Replaced once the real
 * Timer screen (feature.timer) is wired to navigation.
 */
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(Res.string.home_placeholder),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )
    }
}

@PreviewLightDark
@Composable
private fun HomeScreenPreview() {
    FocusBoxTheme { HomeScreen() }
}
