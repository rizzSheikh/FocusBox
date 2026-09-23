package com.rizz.focusbox.feature.timer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizz.focusbox.data.settings.SettingsRepository
import com.rizz.focusbox.data.settings.TimerDurationsSettings
import com.rizz.focusbox.feature.timer.SessionType
import com.rizz.focusbox.feature.timer.TimerConfig
import com.rizz.focusbox.feature.timer.TimerEngine
import com.rizz.focusbox.feature.timer.TimerState
import com.rizz.focusbox.feature.timer.view.event.TimerScreenEvents
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class TimerViewModel() : ViewModel() {

}