package com.rizz.focusbox.feature.stats

import com.rizz.focusbox.db.FocusSession
import kotlinx.datetime.LocalDate

data class DashboardStats(
    val focusTodaySec: Long,
    val currentStreakDays: Int,
    val sessionsTodayCount: Int,
    val dailyAverageSec: Long,
    val last7Days: List<DayTotal>,
    val focusByTask: List<TaskTotal>,
    val recentSessions: List<FocusSession>
)

data class DayTotal(val date: LocalDate, val totalFocusSec: Long)

data class TaskTotal(val taskName: String, val totalFocusSec: Long)

data class HistoryGroup(val date: LocalDate, val totalFocusSec: Long, val sessions: List<FocusSession>)

enum class HistoryFilter { ALL, FOCUS, BREAKS }
