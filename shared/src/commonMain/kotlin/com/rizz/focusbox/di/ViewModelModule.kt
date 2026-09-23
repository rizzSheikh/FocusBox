package com.rizz.focusbox.di

import com.rizz.focusbox.feature.home.viewmodel.HomeViewModel
import com.rizz.focusbox.feature.notificationpermission.viewmodel.NotificationRationaleViewModel
import com.rizz.focusbox.feature.onboarding.viewmodel.OnboardingViewModel
import com.rizz.focusbox.feature.splash.viewmodel.SplashViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::SplashViewModel)
    viewModelOf(::OnboardingViewModel)
    viewModelOf(::NotificationRationaleViewModel)
    viewModelOf(::HomeViewModel)
}
