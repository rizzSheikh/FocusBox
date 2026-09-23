package com.rizz.focusbox.feature.onboarding.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizz.focusbox.data.onboarding.OnboardingRepository
import com.rizz.focusbox.feature.onboarding.view.effect.OnBoardingScreenEffect
import com.rizz.focusbox.feature.onboarding.view.event.OnBoardingScreenEvents
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val onboardingRepository: OnboardingRepository,
) : ViewModel() {

    val hasOnboarded: StateFlow<Boolean> = onboardingRepository.hasOnboarded

    private val _effect = Channel<OnBoardingScreenEffect>()
    val effect: Flow<OnBoardingScreenEffect> get() = _effect.receiveAsFlow()

    fun onEvent(event: OnBoardingScreenEvents) {
        when (event) {
            OnBoardingScreenEvents.OnSkipClick -> onSkip()
            OnBoardingScreenEvents.OnNextClick -> sendEffect(OnBoardingScreenEffect.NavigateToNotificationRationale)
        }
    }

    private fun onSkip() {
        onboardingRepository.setOnboarded()
        sendEffect(OnBoardingScreenEffect.NavigateHome)
    }

    private fun sendEffect(effect: OnBoardingScreenEffect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}
