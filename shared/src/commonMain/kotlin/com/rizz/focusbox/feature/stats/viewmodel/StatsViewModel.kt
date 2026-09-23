package com.rizz.focusbox.feature.stats.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizz.focusbox.data.stats.StatsRepository
import com.rizz.focusbox.feature.stats.model.DashboardStats
import com.rizz.focusbox.feature.stats.StatsCalculator
import com.rizz.focusbox.feature.stats.view.event.StatsScreenEvents
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class StatsViewModel(private val statsRepository: StatsRepository) : ViewModel() {

}
