package com.rizz.focusbox.feature.splash.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rizz.focusbox.feature.splash.view.event.SplashScreenEvents
import com.rizz.focusbox.ui.theme.FocusBoxTheme
import com.rizz.focusbox.ui.theme.PreviewLightDark
import focusbox.shared.generated.resources.Res
import focusbox.shared.generated.resources.ic_app_logo
import focusbox.shared.generated.resources.splash_wordmark
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private const val SPLASH_DURATION_MS = 1200L

@Composable
fun SplashScreen(onEvent: (SplashScreenEvents) -> Unit, modifier: Modifier = Modifier) {
    LaunchedEffect(Unit) {
        delay(SPLASH_DURATION_MS)
        onEvent(SplashScreenEvents.OnTimeout)
    }
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(Res.drawable.ic_app_logo),
            contentDescription = null,
            modifier = Modifier.size(72.dp),
        )
        Text(
            text = stringResource(Res.string.splash_wordmark),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = 16.dp),
        )
    }
}

@PreviewLightDark
@Composable
private fun SplashScreenPreview() {
    FocusBoxTheme { SplashScreen(onEvent = {}) }
}
