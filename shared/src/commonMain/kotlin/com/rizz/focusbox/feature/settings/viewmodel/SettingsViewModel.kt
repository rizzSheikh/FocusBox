package com.rizz.focusbox.feature.settings.viewmodel

import androidx.lifecycle.ViewModel
import com.rizz.focusbox.data.settings.SettingsRepository
import com.rizz.focusbox.data.theme.ThemeRepository

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val themeRepository: ThemeRepository,
) : ViewModel() {

}
