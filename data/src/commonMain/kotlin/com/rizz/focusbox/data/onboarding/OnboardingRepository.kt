package com.rizz.focusbox.data.onboarding

import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.annotation.Single

@Single
class OnboardingRepository(
    private val settings: Settings = Settings(),
) {
    private val hasOnboardedFlow = MutableStateFlow(settings.getBoolean(KEY_HAS_ONBOARDED, false))

    val hasOnboarded: StateFlow<Boolean> = hasOnboardedFlow.asStateFlow()

    fun setOnboarded() {
        settings.putBoolean(KEY_HAS_ONBOARDED, true)
        hasOnboardedFlow.value = true
    }

    private companion object {
        const val KEY_HAS_ONBOARDED = "has_onboarded"
    }
}
