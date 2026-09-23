package com.rizz.focusbox.feature.notificationpermission.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizz.focusbox.data.onboarding.OnboardingRepository
import com.rizz.focusbox.feature.notificationpermission.view.effect.NotificationRationaleScreenEffect
import com.rizz.focusbox.feature.notificationpermission.view.event.NotificationRationaleScreenEvents
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class NotificationRationaleViewModel(
    private val onboardingRepository: OnboardingRepository,
) : ViewModel() {

    private val _effect = Channel<NotificationRationaleScreenEffect>()
    val effect: Flow<NotificationRationaleScreenEffect> get() = _effect.receiveAsFlow()

    fun onEvent(event: NotificationRationaleScreenEvents) {
        when (event) {
            NotificationRationaleScreenEvents.OnPermissionResult,
            NotificationRationaleScreenEvents.OnNotNowClick,
            -> finishOnboarding()
        }
    }

    private fun finishOnboarding() {
        onboardingRepository.setOnboarded()
        viewModelScope.launch { _effect.send(NotificationRationaleScreenEffect.NavigateHome) }
    }
}
