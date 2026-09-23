package com.rizz.focusbox.data.theme

import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.annotation.Single

@Single
class ThemeRepository(
    private val settings: Settings = Settings(),
) {
    private val themeModeFlow = MutableStateFlow(readThemeMode())

    val themeMode: StateFlow<ThemeMode> = themeModeFlow.asStateFlow()

    fun setThemeMode(mode: ThemeMode) {
        settings.putString(KEY_THEME_MODE, mode.name)
        themeModeFlow.value = mode
    }

    private fun readThemeMode(): ThemeMode =
        ThemeMode.fromStored(settings.getStringOrNull(KEY_THEME_MODE))

    private companion object {
        const val KEY_THEME_MODE = "theme_mode"
    }
}
