package com.rizz.focusbox.feature.stats

import com.rizz.focusbox.db.FocusSession
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class StatsCalculatorTest {

    private val zone = TimeZone.UTC
    private val today = LocalDate(2026, 9, 22)

    private fun epochMillisAt(date: LocalDate, hour: Int = 12): Long =
        date.atStartOfDayIn(zone).plus(hour, DateTimeUnit.HOUR).toEpochMilliseconds()

    private fun session(
        taskName: String?,
        type: String,
        date: LocalDate,
        hour: Int = 12,
        plannedDurationSec: Long = 1500,
        actualDurationSec: Long = 1500,
        completed: Long = 1L
    ) = FocusSession(
        id = 0,
        taskName = taskName,
        type = type,
        plannedDurationSec = plannedDurationSec,
        actualDurationSec = actualDurationSec,
        startedAt = epochMillisAt(date, hour),
        endedAt = epochMillisAt(date, hour) + actualDurationSec * 1000,
        completed = completed
    )

    @Test
    fun dashboardStats_with_no_sessions_returns_all_zero() {
        val stats = StatsCalculator.dashboardStats(emptyList(), today, zone)

        assertEquals(0L, stats.focusTodaySec)
        assertEquals(0, stats.currentStreakDays)
        assertEquals(0, stats.sessionsTodayCount)
        assertEquals(0L, stats.dailyAverageSec)
        assertEquals(7, stats.last7Days.size)
        assertTrue(stats.last7Days.all { it.totalFocusSec == 0L })
        assertTrue(stats.focusByTask.isEmpty())
        assertTrue(stats.recentSessions.isEmpty())
    }

    @Test
    fun dashboardStats_counts_todays_completed_focus_sessions() {
        val sessions = listOf(
            session("Write report", "FOCUS", today, hour = 9),
            session("Write report", "FOCUS", today, hour = 11),
            session(null, "SHORT_BREAK", today, hour = 10),
            session("Skipped task", "FOCUS", today, hour = 14, completed = 0L)
        )

        val stats = StatsCalculator.dashboardStats(sessions, today, zone)

        assertEquals(3000L, stats.focusTodaySec)
        assertEquals(2, stats.sessionsTodayCount)
    }

    @Test
    fun dashboardStats_streak_counts_consecutive_days_with_completed_focus() {
        val sessions = listOf(
            session("A", "FOCUS", today),
            session("A", "FOCUS", today.minus(1, DateTimeUnit.DAY)),
            session("A", "FOCUS", today.minus(2, DateTimeUnit.DAY)),
            session("A", "FOCUS", today.minus(4, DateTimeUnit.DAY))
        )

        val stats = StatsCalculator.dashboardStats(sessions, today, zone)

        assertEquals(3, stats.currentStreakDays)
    }

    @Test
    fun dashboardStats_streak_skips_an_unfinished_today() {
        val sessions = listOf(
            session("A", "FOCUS", today.minus(1, DateTimeUnit.DAY)),
            session("A", "FOCUS", today.minus(2, DateTimeUnit.DAY))
        )

        val stats = StatsCalculator.dashboardStats(sessions, today, zone)

        assertEquals(2, stats.currentStreakDays)
    }

    @Test
    fun dashboardStats_streak_is_zero_when_yesterday_has_no_sessions_and_today_is_empty() {
        val sessions = listOf(
            session("A", "FOCUS", today.minus(2, DateTimeUnit.DAY))
        )

        val stats = StatsCalculator.dashboardStats(sessions, today, zone)

        assertEquals(0, stats.currentStreakDays)
    }

    @Test
    fun dashboardStats_focus_by_task_sums_within_last_7_days_and_sorts_descending() {
        val sessions = listOf(
            session("Write report", "FOCUS", today, actualDurationSec = 1200),
            session("Write report", "FOCUS", today.minus(1, DateTimeUnit.DAY), actualDurationSec = 600),
            session("Read book", "FOCUS", today, actualDurationSec = 300),
            session("Write report", "FOCUS", today.minus(8, DateTimeUnit.DAY), actualDurationSec = 9999)
        )

        val stats = StatsCalculator.dashboardStats(sessions, today, zone)

        assertEquals(2, stats.focusByTask.size)
        assertEquals("Write report", stats.focusByTask[0].taskName)
        assertEquals(1800L, stats.focusByTask[0].totalFocusSec)
        assertEquals("Read book", stats.focusByTask[1].taskName)
    }

    @Test
    fun dashboardStats_focus_by_task_excludes_sessions_after_today() {
        val sessions = listOf(
            session("Write report", "FOCUS", today, actualDurationSec = 1200),
            session("Future task", "FOCUS", today.plus(1, DateTimeUnit.DAY), actualDurationSec = 9999)
        )

        val stats = StatsCalculator.dashboardStats(sessions, today, zone)

        assertEquals(1, stats.focusByTask.size)
        assertEquals("Write report", stats.focusByTask[0].taskName)
    }

    @Test
    fun dashboardStats_recent_sessions_returns_five_most_recent_newest_first() {
        val sessions = (0..6).map { i -> session("Task $i", "FOCUS", today, hour = i) }

        val stats = StatsCalculator.dashboardStats(sessions, today, zone)

        assertEquals(5, stats.recentSessions.size)
        assertEquals("Task 6", stats.recentSessions.first().taskName)
    }

    @Test
    fun groupedHistory_groups_by_local_day_and_sums_completed_focus_per_group() {
        val sessions = listOf(
            session("Write report", "FOCUS", today, hour = 10, actualDurationSec = 1500),
            session(null, "SHORT_BREAK", today, hour = 11, actualDurationSec = 300),
            session("Read book", "FOCUS", today.minus(1, DateTimeUnit.DAY), hour = 9, actualDurationSec = 1500)
        )

        val groups = StatsCalculator.groupedHistory(sessions, HistoryFilter.ALL, zone)

        assertEquals(2, groups.size)
        assertEquals(today, groups[0].date)
        assertEquals(1500L, groups[0].totalFocusSec)
        assertEquals(2, groups[0].sessions.size)
    }

    @Test
    fun groupedHistory_shows_actual_not_planned_duration_for_skipped_sessions() {
        val sessions = listOf(
            session("Write API docs", "FOCUS", today, plannedDurationSec = 1500, actualDurationSec = 720, completed = 0L)
        )

        val groups = StatsCalculator.groupedHistory(sessions, HistoryFilter.ALL, zone)

        val skipped = groups.single().sessions.single()
        assertEquals(720L, skipped.actualDurationSec)
        assertEquals(1500L, skipped.plannedDurationSec)
        assertEquals(0L, skipped.completed)
    }

    @Test
    fun groupedHistory_focus_filter_excludes_breaks() {
        val sessions = listOf(
            session("Write report", "FOCUS", today),
            session(null, "SHORT_BREAK", today)
        )

        val groups = StatsCalculator.groupedHistory(sessions, HistoryFilter.FOCUS, zone)

        assertEquals(1, groups.single().sessions.size)
        assertEquals("FOCUS", groups.single().sessions.single().type)
    }

    @Test
    fun groupedHistory_breaks_filter_includes_both_short_and_long() {
        val sessions = listOf(
            session(null, "SHORT_BREAK", today),
            session(null, "LONG_BREAK", today),
            session("Write report", "FOCUS", today)
        )

        val groups = StatsCalculator.groupedHistory(sessions, HistoryFilter.BREAKS, zone)

        assertEquals(2, groups.single().sessions.size)
    }
}
