package com.rizz.focusbox.di

import com.rizz.focusbox.feature.notificationpermission.viewmodel.NotificationRationaleViewModel
import com.rizz.focusbox.feature.onboarding.viewmodel.OnboardingViewModel
import com.rizz.focusbox.feature.settings.viewmodel.SettingsViewModel
import com.rizz.focusbox.feature.splash.viewmodel.SplashViewModel
import com.rizz.focusbox.feature.stats.viewmodel.StatsViewModel
import com.rizz.focusbox.feature.timer.viewmodel.TimerViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::SplashViewModel)
    viewModelOf(::OnboardingViewModel)
    viewModelOf(::NotificationRationaleViewModel)
    viewModelOf(::TimerViewModel)
    viewModelOf(::StatsViewModel)
    viewModelOf(::SettingsViewModel)
}
