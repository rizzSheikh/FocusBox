package com.rizz.focusbox

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rizz.focusbox.data.theme.ThemeRepository
import com.rizz.focusbox.navigation.FocusBoxNavGraph
import com.rizz.focusbox.ui.theme.FocusBoxTheme
import com.rizz.focusbox.ui.theme.resolveDarkTheme
import org.koin.compose.koinInject

@Composable
@Preview
fun App() {
    val themeRepository: ThemeRepository = koinInject()
    val themeMode by themeRepository.themeMode.collectAsStateWithLifecycle()
    FocusBoxTheme(darkTheme = resolveDarkTheme(themeMode)) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            FocusBoxNavGraph(modifier = Modifier.safeContentPadding().fillMaxSize())
        }
    }
}
