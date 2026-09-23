package com.rizz.focusbox.feature.settings.viewmodel

import com.rizz.focusbox.data.theme.ThemeMode

data class SettingsUiState(
    val themeMode: ThemeMode = ThemeMode.System,
)
