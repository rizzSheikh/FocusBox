package com.rizz.focusbox.feature.splash.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizz.focusbox.data.onboarding.OnboardingRepository
import com.rizz.focusbox.feature.splash.view.effect.SplashScreenEffect
import com.rizz.focusbox.feature.splash.view.event.SplashScreenEvents
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    private val onboardingRepository: OnboardingRepository,
) : ViewModel() {

    private val _effect = Channel<SplashScreenEffect>()
    val effect: Flow<SplashScreenEffect> get() = _effect.receiveAsFlow()

    fun onEvent(event: SplashScreenEvents) {
        when (event) {
            SplashScreenEvents.OnTimeout -> onTimeout()
        }
    }

    private fun onTimeout() {
        val destination = if (onboardingRepository.hasOnboarded.value) {
            SplashScreenEffect.NavigateToHome
        } else {
            SplashScreenEffect.NavigateToOnboarding
        }
        viewModelScope.launch { _effect.send(destination) }
    }
}
