package com.rizz.focusbox.feature.stats

import com.rizz.focusbox.db.FocusSession
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime

object StatsCalculator {

    private const val FOCUS = "FOCUS"
    private const val COMPLETED = 1L

    private fun FocusSession.localDate(zone: TimeZone): LocalDate =
        Instant.fromEpochMilliseconds(startedAt).toLocalDateTime(zone).date

    fun dashboardStats(
        sessions: List<FocusSession>,
        today: LocalDate,
        zone: TimeZone = TimeZone.currentSystemDefault()
    ): DashboardStats {
        val completedFocus = sessions.filter { it.type == FOCUS && it.completed == COMPLETED }

        fun totalFor(date: LocalDate): Long =
            completedFocus.filter { it.localDate(zone) == date }.sumOf { it.actualDurationSec }

        val focusTodaySec = totalFor(today)
        val sessionsTodayCount = completedFocus.count { it.localDate(zone) == today }

        var streak = if (focusTodaySec > 0) 1 else 0
        var cursor = today.minus(1, DateTimeUnit.DAY)
        while (totalFor(cursor) > 0) {
            streak++
            cursor = cursor.minus(1, DateTimeUnit.DAY)
        }

        val last7Days = (6 downTo 0).map { offset ->
            val date = today.minus(offset, DateTimeUnit.DAY)
            DayTotal(date, totalFor(date))
        }

        val dailyAverageSec = last7Days.sumOf { it.totalFocusSec } / 7

        val windowStart = today.minus(6, DateTimeUnit.DAY)
        val focusByTask = completedFocus
            .filter { it.taskName != null && it.localDate(zone) >= windowStart }
            .groupBy { it.taskName!! }
            .map { (taskName, entries) -> TaskTotal(taskName, entries.sumOf { it.actualDurationSec }) }
            .sortedByDescending { it.totalFocusSec }

        val recentSessions = sessions.sortedByDescending { it.startedAt }.take(5)

        return DashboardStats(
            focusTodaySec = focusTodaySec,
            currentStreakDays = streak,
            sessionsTodayCount = sessionsTodayCount,
            dailyAverageSec = dailyAverageSec,
            last7Days = last7Days,
            focusByTask = focusByTask,
            recentSessions = recentSessions
        )
    }
}
