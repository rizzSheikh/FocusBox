# Stats & History Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build the `feature.stats` domain layer — a reactive read-only repository plus a pure calculator — that turns the `FocusSession` table into the dashboard stats and the date-grouped, filterable session history from the Figma design.

**Architecture:** `StatsRepository` (read-only, separate from `feature.timer`'s write-only `SessionRepository`) exposes `Flow<List<FocusSession>>` over the existing `FocusSession` table via SQLDelight's reactive queries. `StatsCalculator` is a pure object — no DB, no Android, no coroutines — that turns that flat list into `DashboardStats` and `List<HistoryGroup>`, using `kotlinx-datetime` for correct local-calendar-day boundaries. This mirrors `TimerEngine`'s separation of pure domain logic from I/O.

**Tech Stack:** Kotlin Multiplatform, SQLDelight 2.4.0, `kotlinx-datetime` 0.6.1 (new dependency), Koin Annotations 2.3.1, kotlin.test.

**Spec:** `docs/superpowers/specs/2026-09-22-stats-history-design.md`

## Global Constraints

- Package root: `com.rizz.focusbox`. This feature's code lives under `com.rizz.focusbox.feature.stats`.
- DI: annotate injectable classes with `@Single` from `org.koin.core.annotation` — `@ComponentScan("com.rizz.focusbox")` in `AppModule.kt` picks them up automatically. No manual module registration.
- `StatsRepository` is read-only (`observeSessions()` only) and lives in `feature.stats`, separate from `feature.timer`'s write-only `SessionRepository` — no cross-feature dependency between the two.
- The read model reuses the SQLDelight-generated `com.rizz.focusbox.db.FocusSession` type directly (fields: `id: Long`, `taskName: String?`, `type: String`, `plannedDurationSec: Long`, `actualDurationSec: Long`, `startedAt: Long`, `endedAt: Long?`, `completed: Long` — `completed` is `Long` 0/1, not `Boolean`, since no SQLDelight column adapter is used). No parallel domain type.
- `StatsCalculator` must be a PURE object: no DB, no Android dependency, no coroutines, no internal clock — "today" and "timezone" are always parameters supplied by the caller, never read internally, so every rule is deterministic and testable.
- No new SQL aggregate queries. All aggregation is computed in Kotlin over the existing `selectAll()` query's result.
- No ViewModels, no Compose UI in this plan — domain layer only, matching how the Timer slice stopped at `TimerEngine`.
- Android tests run via `./gradlew :shared:testAndroidHostTest`; iOS via `./gradlew :shared:iosSimulatorArm64Test` (falls back to `./gradlew :shared:compileKotlinIosSimulatorArm64` if simulator execution is unavailable on the host, e.g. Linux); Android app build via `./gradlew :androidApp:assembleDebug` — all three must stay green.

## Global Design Notes (apply across all tasks)

**Aggregation rules** (from the spec — copy verbatim, these are the binding definitions):
- "Focus today" / chart / "Focus by task": sum of `actualDurationSec` where `type = "FOCUS"` and `completed = 1L`, bucketed by the session's local calendar day (from `startedAt`).
- "Sessions today": count of sessions where `type = "FOCUS"`, `completed = 1L`, local calendar day = today.
- "Current streak": if today has ≥1 completed `FOCUS` session, streak starts at 1; otherwise streak starts at 0. Either way, then walk backward one calendar day at a time starting from yesterday, adding 1 for each consecutive day that also has ≥1 completed `FOCUS` session, stopping at the first day with none.
- "Daily average": sum of the last 7 calendar days' focus totals (today plus the 6 preceding days), divided by 7.
- "Focus by task": non-null `taskName`, completed `FOCUS` sessions only, within the same 7-day window (today plus 6 preceding days), grouped by `taskName`, summed by `actualDurationSec`, sorted descending by total.
- "Recent sessions": the 5 most recent sessions by `startedAt` descending, any type/outcome.
- History grouping: bucket by local calendar day of `startedAt`; each group's `totalFocusSec` is the sum of completed `FOCUS` `actualDurationSec` within that group only (not breaks); groups sorted newest-day-first.
- History filter: `ALL` = every session; `FOCUS` = `type == "FOCUS"`; `BREAKS` = `type == "SHORT_BREAK" || type == "LONG_BREAK"`.

**Domain model** (final shape, built across Tasks 1-3):

```kotlin
// StatsModels.kt
data class DashboardStats(
    val focusTodaySec: Long,
    val currentStreakDays: Int,
    val sessionsTodayCount: Int,
    val dailyAverageSec: Long,
    val last7Days: List<DayTotal>,      // oldest to newest, 7 entries
    val focusByTask: List<TaskTotal>,   // sorted descending by totalFocusSec
    val recentSessions: List<FocusSession>
)

data class DayTotal(val date: LocalDate, val totalFocusSec: Long)

data class TaskTotal(val taskName: String, val totalFocusSec: Long)

data class HistoryGroup(val date: LocalDate, val totalFocusSec: Long, val sessions: List<FocusSession>)

enum class HistoryFilter { ALL, FOCUS, BREAKS }
```

`StatsCalculator` public API (final shape, built across Tasks 2 and 3):
- `fun dashboardStats(sessions: List<FocusSession>, today: LocalDate, zone: TimeZone = TimeZone.currentSystemDefault()): DashboardStats`
- `fun groupedHistory(sessions: List<FocusSession>, filter: HistoryFilter, zone: TimeZone = TimeZone.currentSystemDefault()): List<HistoryGroup>`

---

### Task 1: kotlinx-datetime dependency and StatsRepository

**Files:**
- Modify: `gradle/libs.versions.toml`
- Modify: `shared/build.gradle.kts`
- Create: `shared/src/commonMain/kotlin/com/rizz/focusbox/feature/stats/StatsRepository.kt`
- Create: `shared/src/commonMain/kotlin/com/rizz/focusbox/feature/stats/SqlDelightStatsRepository.kt`
- Test: `shared/src/androidHostTest/kotlin/com/rizz/focusbox/feature/stats/SqlDelightStatsRepositoryTest.kt`

**Interfaces:**
- Produces: `interface StatsRepository { fun observeSessions(): Flow<List<FocusSession>> }`, `class SqlDelightStatsRepository(db: FocusBoxDatabase) : StatsRepository`. Tasks 2 and 3's `StatsCalculator` consumes `List<FocusSession>` (the same type this repository's flow emits), not `StatsRepository` itself — `StatsCalculator` is pure and takes a plain list, decoupled from the repository.

- [ ] **Step 1: Add the kotlinx-datetime dependency**

In `gradle/libs.versions.toml`, add this line under `[versions]` (alphabetically near the other entries):

```toml
kotlinxDatetime = "0.6.1"
```

Add this line under `[libraries]` (next to the other cross-cutting libraries):

```toml
kotlinx-datetime = { module = "org.jetbrains.kotlinx:kotlinx-datetime", version.ref = "kotlinxDatetime" }
```

In `shared/build.gradle.kts`, inside the existing `commonMain.dependencies { ... }` block, add:

```kotlin
            implementation(libs.kotlinx.datetime)
```

- [ ] **Step 2: Add the repository interface**

Create `shared/src/commonMain/kotlin/com/rizz/focusbox/feature/stats/StatsRepository.kt`:

```kotlin
package com.rizz.focusbox.feature.stats

import com.rizz.focusbox.db.FocusSession
import kotlinx.coroutines.flow.Flow

interface StatsRepository {
    fun observeSessions(): Flow<List<FocusSession>>
}
```

- [ ] **Step 3: Add the SQLDelight-backed implementation**

Create `shared/src/commonMain/kotlin/com/rizz/focusbox/feature/stats/SqlDelightStatsRepository.kt`:

```kotlin
package com.rizz.focusbox.feature.stats

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.rizz.focusbox.db.FocusBoxDatabase
import com.rizz.focusbox.db.FocusSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single(binds = [StatsRepository::class])
class SqlDelightStatsRepository(private val db: FocusBoxDatabase) : StatsRepository {
    override fun observeSessions(): Flow<List<FocusSession>> =
        db.focusSessionQueries.selectAll().asFlow().mapToList(Dispatchers.Default)
}
```

- [ ] **Step 4: Write the failing repository test**

Create `shared/src/androidHostTest/kotlin/com/rizz/focusbox/feature/stats/SqlDelightStatsRepositoryTest.kt`:

```kotlin
package com.rizz.focusbox.feature.stats

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.rizz.focusbox.db.FocusBoxDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class SqlDelightStatsRepositoryTest {

    private fun newDatabase(): FocusBoxDatabase {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        FocusBoxDatabase.Schema.create(driver)
        return FocusBoxDatabase(driver)
    }

    @Test
    fun observeSessions_emits_inserted_rows() = runBlocking {
        val db = newDatabase()
        val repository = SqlDelightStatsRepository(db)

        db.focusSessionQueries.insertSession(
            taskName = "Write report",
            type = "FOCUS",
            plannedDurationSec = 1500,
            actualDurationSec = 1500,
            startedAt = 1000L,
            endedAt = 2500L,
            completed = 1L
        )

        val sessions = repository.observeSessions().first()

        assertEquals(1, sessions.size)
        assertEquals("Write report", sessions[0].taskName)
        assertEquals(1L, sessions[0].completed)
    }

    @Test
    fun observeSessions_on_empty_table_emits_empty_list() = runBlocking {
        val db = newDatabase()
        val repository = SqlDelightStatsRepository(db)

        val sessions = repository.observeSessions().first()

        assertEquals(emptyList(), sessions)
    }
}
```

- [ ] **Step 5: Run the tests to verify they pass**

Run: `./gradlew :shared:testAndroidHostTest --tests "com.rizz.focusbox.feature.stats.SqlDelightStatsRepositoryTest"`
Expected: both tests PASS. (As with the prior slice's Task 1, the repository is written together with its test rather than in a strict red/green order — the query code doesn't exist until Steps 2-3 are done, so compilation itself is the first signal.)

- [ ] **Step 6: Commit**

```bash
git add gradle/libs.versions.toml shared/build.gradle.kts \
        shared/src/commonMain/kotlin/com/rizz/focusbox/feature/stats/StatsRepository.kt \
        shared/src/commonMain/kotlin/com/rizz/focusbox/feature/stats/SqlDelightStatsRepository.kt \
        shared/src/androidHostTest/kotlin/com/rizz/focusbox/feature/stats/SqlDelightStatsRepositoryTest.kt
git commit -m "feat: add kotlinx-datetime and StatsRepository"
```

---

### Task 2: Domain models and dashboardStats()

**Files:**
- Create: `shared/src/commonMain/kotlin/com/rizz/focusbox/feature/stats/StatsModels.kt`
- Create: `shared/src/commonMain/kotlin/com/rizz/focusbox/feature/stats/StatsCalculator.kt`
- Test: `shared/src/commonTest/kotlin/com/rizz/focusbox/feature/stats/StatsCalculatorTest.kt`

**Interfaces:**
- Consumes: `com.rizz.focusbox.db.FocusSession` (the SQLDelight-generated type; already exists from the prior slice).
- Produces: `DashboardStats`, `DayTotal`, `TaskTotal`, `HistoryGroup`, `HistoryFilter` (all in `StatsModels.kt`), `object StatsCalculator` with `fun dashboardStats(...)`. Task 3 adds `fun groupedHistory(...)` to this same object.

- [ ] **Step 1: Add the domain models**

Create `shared/src/commonMain/kotlin/com/rizz/focusbox/feature/stats/StatsModels.kt`:

```kotlin
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
```

- [ ] **Step 2: Write the failing tests for dashboardStats()**

Create `shared/src/commonTest/kotlin/com/rizz/focusbox/feature/stats/StatsCalculatorTest.kt`:

```kotlin
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
    fun dashboardStats_recent_sessions_returns_five_most_recent_newest_first() {
        val sessions = (0..6).map { i -> session("Task $i", "FOCUS", today, hour = i) }

        val stats = StatsCalculator.dashboardStats(sessions, today, zone)

        assertEquals(5, stats.recentSessions.size)
        assertEquals("Task 6", stats.recentSessions.first().taskName)
    }
}
```

- [ ] **Step 3: Run the tests to verify they fail**

Run: `./gradlew :shared:testAndroidHostTest --tests "com.rizz.focusbox.feature.stats.StatsCalculatorTest"`
Expected: FAIL to compile — `StatsCalculator` is not defined yet.

- [ ] **Step 4: Implement StatsCalculator.dashboardStats()**

Create `shared/src/commonMain/kotlin/com/rizz/focusbox/feature/stats/StatsCalculator.kt`:

```kotlin
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
```

- [ ] **Step 5: Run the tests to verify they pass**

Run: `./gradlew :shared:testAndroidHostTest --tests "com.rizz.focusbox.feature.stats.StatsCalculatorTest"`
Expected: all 7 tests PASS.

- [ ] **Step 6: Commit**

```bash
git add shared/src/commonMain/kotlin/com/rizz/focusbox/feature/stats/StatsModels.kt \
        shared/src/commonMain/kotlin/com/rizz/focusbox/feature/stats/StatsCalculator.kt \
        shared/src/commonTest/kotlin/com/rizz/focusbox/feature/stats/StatsCalculatorTest.kt
git commit -m "feat: add StatsCalculator.dashboardStats()"
```

---

### Task 3: groupedHistory() and full build verification

**Files:**
- Modify: `shared/src/commonMain/kotlin/com/rizz/focusbox/feature/stats/StatsCalculator.kt`
- Modify: `shared/src/commonTest/kotlin/com/rizz/focusbox/feature/stats/StatsCalculatorTest.kt`

**Interfaces:**
- Consumes: everything from Tasks 1-2.
- Produces: `StatsCalculator.groupedHistory(sessions: List<FocusSession>, filter: HistoryFilter, zone: TimeZone = TimeZone.currentSystemDefault()): List<HistoryGroup>`, completing `StatsCalculator`'s public API per the plan's "Global Design Notes". No later task depends on this one.

- [ ] **Step 1: Write the failing tests for groupedHistory()**

Append these test functions inside the `StatsCalculatorTest` class in `shared/src/commonTest/kotlin/com/rizz/focusbox/feature/stats/StatsCalculatorTest.kt` (before the closing `}` of the class):

```kotlin
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
```

- [ ] **Step 2: Run the tests to verify they fail**

Run: `./gradlew :shared:testAndroidHostTest --tests "com.rizz.focusbox.feature.stats.StatsCalculatorTest"`
Expected: FAIL to compile — `groupedHistory()` is not defined yet.

- [ ] **Step 3: Implement groupedHistory()**

In `shared/src/commonMain/kotlin/com/rizz/focusbox/feature/stats/StatsCalculator.kt`, add this function to the `StatsCalculator` object, after `dashboardStats` (no new imports needed — `HistoryGroup`/`HistoryFilter` are in the same package):

```kotlin
    fun groupedHistory(
        sessions: List<FocusSession>,
        filter: HistoryFilter,
        zone: TimeZone = TimeZone.currentSystemDefault()
    ): List<HistoryGroup> {
        val filtered = when (filter) {
            HistoryFilter.ALL -> sessions
            HistoryFilter.FOCUS -> sessions.filter { it.type == FOCUS }
            HistoryFilter.BREAKS -> sessions.filter { it.type == "SHORT_BREAK" || it.type == "LONG_BREAK" }
        }

        return filtered
            .groupBy { it.localDate(zone) }
            .map { (date, entries) ->
                val sortedEntries = entries.sortedByDescending { it.startedAt }
                val totalFocusSec = sortedEntries
                    .filter { it.type == FOCUS && it.completed == COMPLETED }
                    .sumOf { it.actualDurationSec }
                HistoryGroup(date, totalFocusSec, sortedEntries)
            }
            .sortedByDescending { it.date }
    }
```

- [ ] **Step 4: Run the tests to verify they pass**

Run: `./gradlew :shared:testAndroidHostTest --tests "com.rizz.focusbox.feature.stats.StatsCalculatorTest"`
Expected: all 11 tests PASS.

- [ ] **Step 5: Full multi-target build verification**

Run each of these and confirm all succeed:

```bash
./gradlew :shared:testAndroidHostTest
./gradlew :shared:iosSimulatorArm64Test
./gradlew :androidApp:assembleDebug
```

Expected: all three BUILD SUCCESSFUL. On a Linux host, `:shared:iosSimulatorArm64Test`'s actual simulator-execution tasks will be skipped by Gradle itself (no simulator available) — if so, additionally run `./gradlew :shared:compileKotlinIosSimulatorArm64` and confirm it succeeds, and note the substitution in the report; this is an environment limitation, not something to work around. Expect `StatsCalculatorTest` (11 tests) and `SqlDelightStatsRepositoryTest` (2 tests) passing, alongside all tests from the prior slice (`TimerEngineTest`, `SqlDelightSessionRepositoryTest`) still green.

- [ ] **Step 6: Commit**

```bash
git add shared/src/commonMain/kotlin/com/rizz/focusbox/feature/stats/StatsCalculator.kt \
        shared/src/commonTest/kotlin/com/rizz/focusbox/feature/stats/StatsCalculatorTest.kt
git commit -m "feat: add StatsCalculator.groupedHistory()"
```
