package com.rizz.focusbox.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalFocusBoxExtraColors = staticCompositionLocalOf {
    FocusBoxExtraColors(
        amberContainer = Color.Unspecified,
        onAmberContainer = Color.Unspecified,
    )
}

object FocusBoxTheme {
    val extraColors: FocusBoxExtraColors
        @Composable
        get() = LocalFocusBoxExtraColors.current
}

@Composable
fun FocusBoxTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) FocusBoxDarkColorScheme else FocusBoxLightColorScheme
    val extraColors = if (darkTheme) FocusBoxDarkExtraColors else FocusBoxLightExtraColors

    CompositionLocalProvider(LocalFocusBoxExtraColors provides extraColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content,
        )
    }
}

@Composable
fun resolveDarkTheme(themeMode: ThemeMode): Boolean =
    when (themeMode) {
        ThemeMode.System -> isSystemInDarkTheme()
        ThemeMode.Light -> false
        ThemeMode.Dark -> true
    }
