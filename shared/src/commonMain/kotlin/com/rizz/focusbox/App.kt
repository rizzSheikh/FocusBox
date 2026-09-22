package com.rizz.focusbox

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rizz.focusbox.ui.theme.FocusBoxTheme
import com.rizz.focusbox.ui.theme.ThemeMode
import com.rizz.focusbox.ui.theme.ThemeRepository
import com.rizz.focusbox.ui.theme.resolveDarkTheme
import focusbox.shared.generated.resources.Res
import focusbox.shared.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject

@Composable
@Preview
fun App() {
    val themeRepository: ThemeRepository = koinInject()
    val themeMode by themeRepository.themeMode.collectAsStateWithLifecycle()
    FocusBoxTheme(darkTheme = resolveDarkTheme(themeMode)) {
        FocusBoxRootContent(
            themeMode = themeMode,
            onThemeModeChange = themeRepository::setThemeMode,
        )
    }
}

@Composable
internal fun FocusBoxRootContent(
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit,
) {
    var showContent by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .safeContentPadding()
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Theme: ${themeMode.name}",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Button(onClick = {
            onThemeModeChange(
                when (themeMode) {
                    ThemeMode.System -> ThemeMode.Light
                    ThemeMode.Light -> ThemeMode.Dark
                    ThemeMode.Dark -> ThemeMode.System
                },
            )
        }) {
            Text("Cycle theme")
        }
        Button(onClick = { showContent = !showContent }) {
            Text("Click me!")
        }
        AnimatedVisibility(showContent) {
            val greeting = remember { Greeting().greet() }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(painterResource(Res.drawable.compose_multiplatform), null)
                Text("Compose: $greeting")
            }
        }
    }
}

@Preview
@Composable
private fun FocusBoxRootPreview() {
    FocusBoxTheme(darkTheme = false) {
        FocusBoxRootContent(
            themeMode = ThemeMode.Light,
            onThemeModeChange = {},
        )
    }
}
