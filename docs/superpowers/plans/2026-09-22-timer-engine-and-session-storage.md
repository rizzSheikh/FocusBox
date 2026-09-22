# TimerEngine and Session Storage Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Implement the `TimerEngine` state machine in `commonMain` and extend the `FocusSession` SQLDelight table so completed/skipped sessions are persisted, matching the approved architecture spec.

**Architecture:** `TimerEngine` is a Koin singleton holding a `StateFlow<TimerState>`. It is a pure, synchronous domain object with zero Android/coroutine-dispatch dependencies — callers control threading. On session completion or skip it persists a record through a `SessionRepository` interface, implemented by `SqlDelightSessionRepository`. This keeps the engine testable in `commonTest` with a fake repository, and the repository testable in `androidHostTest` with a real in-memory SQLDelight database.

**Tech Stack:** Kotlin Multiplatform, SQLDelight 2.4.0, Koin Annotations 2.3.1 (compiler plugin, no KSP), kotlin.test.

**Spec:** `docs/superpowers/specs/2026-09-22-focusbox-architecture-design.md`

## Global Constraints

- Package root: `com.rizz.focusbox`. Feature code lives under `com.rizz.focusbox.feature.timer` (feature-based packages per `AGENTS.md`).
- DI: annotate injectable classes with `@Single`/`@Factory` from `org.koin.core.annotation` — `@ComponentScan("com.rizz.focusbox")` in `AppModule.kt` picks them up automatically. No manual module registration.
- Database: SQLDelight, package `com.rizz.focusbox.db`, database class `FocusBoxDatabase`.
- No Task table, no Settings table in this plan — out of scope (see spec's "Out of scope for this pass"). `TimerConfig` is a plain parameter the caller supplies; wiring it to a persisted Settings table is a later plan.
- Android tests run via `./gradlew :shared:testAndroidHostTest`; iOS tests via `./gradlew :shared:iosSimulatorArm64Test`; Android app build via `./gradlew :androidApp:assembleDebug` (all three must stay green per `AGENTS.md` status).

---

## File Structure

- `shared/src/commonMain/sqldelight/com/rizz/focusbox/db/FocusSession.sq` — **modify**: replace the placeholder schema with the real session columns.
- `shared/src/commonMain/kotlin/com/rizz/focusbox/feature/timer/SessionRepository.kt` — **create**: `FocusSessionRecord` data class + `SessionRepository` interface.
- `shared/src/commonMain/kotlin/com/rizz/focusbox/feature/timer/SqlDelightSessionRepository.kt` — **create**: real SQLDelight-backed implementation.
- `shared/src/commonMain/kotlin/com/rizz/focusbox/feature/timer/TimerModels.kt` — **create**: `SessionType`, `TimerConfig`, `ActiveSession`, `TimerState`.
- `shared/src/commonMain/kotlin/com/rizz/focusbox/feature/timer/Clock.kt` — **create**: `expect fun currentTimeMillis(): Long`.
- `shared/src/androidMain/kotlin/com/rizz/focusbox/feature/timer/Clock.android.kt` — **create**: Android actual.
- `shared/src/iosMain/kotlin/com/rizz/focusbox/feature/timer/Clock.ios.kt` — **create**: iOS actual.
- `shared/src/commonMain/kotlin/com/rizz/focusbox/feature/timer/TimerEngine.kt` — **create**: the state machine.
- `shared/src/commonTest/kotlin/com/rizz/focusbox/feature/timer/TimerEngineTest.kt` — **create**: pure unit tests with a fake repository.
- `shared/src/androidHostTest/kotlin/com/rizz/focusbox/feature/timer/SqlDelightSessionRepositoryTest.kt` — **create**: repository tests against an in-memory SQLite DB.
- `gradle/libs.versions.toml` — **modify**: add the JDBC SQLite driver (test-only) library entry.
- `shared/build.gradle.kts` — **modify**: add that driver to the `androidHostTest` source set's dependencies.

## Global Design Notes (apply across all tasks)

```kotlin
// TimerState (shared/.../feature/timer/TimerModels.kt)
enum class SessionType { FOCUS, SHORT_BREAK, LONG_BREAK }

data class TimerConfig(
    val focusSec: Int,
    val shortBreakSec: Int,
    val longBreakSec: Int,
    val longBreakInterval: Int
) {
    companion object {
        val DEFAULT = TimerConfig(
            focusSec = 25 * 60,
            shortBreakSec = 5 * 60,
            longBreakSec = 15 * 60,
            longBreakInterval = 4
        )
    }
}

data class ActiveSession(
    val type: SessionType,
    val taskName: String?,
    val totalSec: Int,
    val startedAtEpochMillis: Long
)

sealed interface TimerState {
    data object Idle : TimerState
    data class Running(val session: ActiveSession, val remainingSec: Int) : TimerState
    data class Paused(val session: ActiveSession, val remainingSec: Int) : TimerState
    data class Complete(val session: ActiveSession) : TimerState
}
```

`TimerEngine` public API (final shape, built across Tasks 2 and 3):
- `val state: StateFlow<TimerState>`
- `fun start(taskName: String?, config: TimerConfig = TimerConfig.DEFAULT)` — valid from `Idle` or `Complete`.
- `fun pause()` — valid from `Running`.
- `fun resume()` — valid from `Paused`.
- `fun tick()` — valid from `Running`; no-op otherwise.
- `fun skip()` — valid from `Running` or `Paused`.
- `fun startBreak(config: TimerConfig = TimerConfig.DEFAULT)` — valid from `Complete`.

---

### Task 1: Session schema and repository

**Files:**
- Modify: `shared/src/commonMain/sqldelight/com/rizz/focusbox/db/FocusSession.sq`
- Create: `shared/src/commonMain/kotlin/com/rizz/focusbox/feature/timer/SessionRepository.kt`
- Create: `shared/src/commonMain/kotlin/com/rizz/focusbox/feature/timer/SqlDelightSessionRepository.kt`
- Modify: `gradle/libs.versions.toml`
- Modify: `shared/build.gradle.kts`
- Test: `shared/src/androidHostTest/kotlin/com/rizz/focusbox/feature/timer/SqlDelightSessionRepositoryTest.kt`

**Interfaces:**
- Produces: `data class FocusSessionRecord(taskName: String?, type: String, plannedDurationSec: Long, actualDurationSec: Long, startedAt: Long, endedAt: Long?, completed: Boolean)`, `interface SessionRepository { fun insertSession(session: FocusSessionRecord) }`, `class SqlDelightSessionRepository(db: FocusBoxDatabase) : SessionRepository`. `TimerEngine` (Task 2) consumes `SessionRepository`.

- [ ] **Step 1: Replace the FocusSession schema**

Replace the entire contents of `shared/src/commonMain/sqldelight/com/rizz/focusbox/db/FocusSession.sq` with:

```sql
CREATE TABLE FocusSession (
    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    taskName TEXT,
    type TEXT NOT NULL,
    plannedDurationSec INTEGER NOT NULL,
    actualDurationSec INTEGER NOT NULL,
    startedAt INTEGER NOT NULL,
    endedAt INTEGER,
    completed INTEGER NOT NULL
);

selectAll:
SELECT * FROM FocusSession ORDER BY startedAt DESC;

insertSession:
INSERT INTO FocusSession(taskName, type, plannedDurationSec, actualDurationSec, startedAt, endedAt, completed)
VALUES (?, ?, ?, ?, ?, ?, ?);

deleteAll:
DELETE FROM FocusSession;
```

No code currently references the old `durationMinutes` column or old `insertSession` signature (verified via grep), so this is a safe breaking change.

- [ ] **Step 2: Add the repository interface and record type**

Create `shared/src/commonMain/kotlin/com/rizz/focusbox/feature/timer/SessionRepository.kt`:

```kotlin
package com.rizz.focusbox.feature.timer

data class FocusSessionRecord(
    val taskName: String?,
    val type: String,
    val plannedDurationSec: Long,
    val actualDurationSec: Long,
    val startedAt: Long,
    val endedAt: Long?,
    val completed: Boolean
)

interface SessionRepository {
    fun insertSession(session: FocusSessionRecord)
}
```

- [ ] **Step 3: Add the SQLDelight-backed implementation**

Create `shared/src/commonMain/kotlin/com/rizz/focusbox/feature/timer/SqlDelightSessionRepository.kt`:

```kotlin
package com.rizz.focusbox.feature.timer

import com.rizz.focusbox.db.FocusBoxDatabase
import org.koin.core.annotation.Single

@Single(binds = [SessionRepository::class])
class SqlDelightSessionRepository(private val db: FocusBoxDatabase) : SessionRepository {
    override fun insertSession(session: FocusSessionRecord) {
        db.focusSessionQueries.insertSession(
            taskName = session.taskName,
            type = session.type,
            plannedDurationSec = session.plannedDurationSec,
            actualDurationSec = session.actualDurationSec,
            startedAt = session.startedAt,
            endedAt = session.endedAt,
            completed = if (session.completed) 1L else 0L
        )
    }
}
```

- [ ] **Step 4: Add a JDBC in-memory SQLite driver for host tests**

In `gradle/libs.versions.toml`, add this line under `[libraries]` (next to the other `sqldelight-*` entries):

```toml
sqldelight-sqlite-driver = { module = "app.cash.sqldelight:sqlite-driver", version.ref = "sqldelight" }
```

In `shared/build.gradle.kts`, inside the existing `sourceSets { ... }` block, add a new block alongside `commonTest.dependencies { ... }`:

```kotlin
        androidHostTest.dependencies {
            implementation(libs.sqldelight.sqlite.driver)
        }
```

- [ ] **Step 5: Write the failing repository test**

Create `shared/src/androidHostTest/kotlin/com/rizz/focusbox/feature/timer/SqlDelightSessionRepositoryTest.kt`:

```kotlin
package com.rizz.focusbox.feature.timer

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.rizz.focusbox.db.FocusBoxDatabase
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class SqlDelightSessionRepositoryTest {

    private fun newDatabase(): FocusBoxDatabase {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        FocusBoxDatabase.Schema.create(driver)
        return FocusBoxDatabase(driver)
    }

    @Test
    fun insertSession_persists_a_completed_focus_session() {
        val db = newDatabase()
        val repository = SqlDelightSessionRepository(db)

        repository.insertSession(
            FocusSessionRecord(
                taskName = "Write report",
                type = "FOCUS",
                plannedDurationSec = 1500,
                actualDurationSec = 1500,
                startedAt = 1000L,
                endedAt = 2500L,
                completed = true
            )
        )

        val rows = db.focusSessionQueries.selectAll().executeAsList()
        assertEquals(1, rows.size)
        assertEquals("Write report", rows[0].taskName)
        assertEquals("FOCUS", rows[0].type)
        assertEquals(1500L, rows[0].plannedDurationSec)
        assertEquals(1500L, rows[0].actualDurationSec)
        assertEquals(1000L, rows[0].startedAt)
        assertEquals(2500L, rows[0].endedAt)
        assertEquals(1L, rows[0].completed)
    }

    @Test
    fun insertSession_persists_a_skipped_session_with_null_task_name() {
        val db = newDatabase()
        val repository = SqlDelightSessionRepository(db)

        repository.insertSession(
            FocusSessionRecord(
                taskName = null,
                type = "SHORT_BREAK",
                plannedDurationSec = 300,
                actualDurationSec = 120,
                startedAt = 1000L,
                endedAt = 1120L,
                completed = false
            )
        )

        val row = db.focusSessionQueries.selectAll().executeAsList().single()
        assertNull(row.taskName)
        assertEquals(0L, row.completed)
    }
}
```

- [ ] **Step 6: Run the test to verify it passes**

Run: `./gradlew :shared:testAndroidHostTest --tests "com.rizz.focusbox.feature.timer.SqlDelightSessionRepositoryTest"`
Expected: both tests PASS. (Schema and repository are written together, so there is no separate "fails first" step here — the SQLDelight query code does not exist until Step 1-3 are done, so compilation itself is the red/green signal: confirm the module fails to compile before Step 1-3 by checking `git diff` shows nothing yet, then these steps make it compile and pass.)

- [ ] **Step 7: Commit**

```bash
git add shared/src/commonMain/sqldelight/com/rizz/focusbox/db/FocusSession.sq \
        shared/src/commonMain/kotlin/com/rizz/focusbox/feature/timer/SessionRepository.kt \
        shared/src/commonMain/kotlin/com/rizz/focusbox/feature/timer/SqlDelightSessionRepository.kt \
        shared/src/androidHostTest/kotlin/com/rizz/focusbox/feature/timer/SqlDelightSessionRepositoryTest.kt \
        gradle/libs.versions.toml shared/build.gradle.kts
git commit -m "feat: extend FocusSession schema and add SessionRepository"
```

---

### Task 2: Timer domain models and core engine (start/pause/resume/tick)

**Files:**
- Create: `shared/src/commonMain/kotlin/com/rizz/focusbox/feature/timer/TimerModels.kt`
- Create: `shared/src/commonMain/kotlin/com/rizz/focusbox/feature/timer/Clock.kt`
- Create: `shared/src/androidMain/kotlin/com/rizz/focusbox/feature/timer/Clock.android.kt`
- Create: `shared/src/iosMain/kotlin/com/rizz/focusbox/feature/timer/Clock.ios.kt`
- Create: `shared/src/commonMain/kotlin/com/rizz/focusbox/feature/timer/TimerEngine.kt`
- Test: `shared/src/commonTest/kotlin/com/rizz/focusbox/feature/timer/TimerEngineTest.kt`

**Interfaces:**
- Consumes: `SessionRepository`, `FocusSessionRecord` (Task 1).
- Produces: `SessionType`, `TimerConfig`, `ActiveSession`, `TimerState` (all in `TimerModels.kt`), `class TimerEngine(repository: SessionRepository)` with `val state: StateFlow<TimerState>`, `fun start(...)`, `fun pause()`, `fun resume()`, `fun tick()`. Task 3 adds `fun skip()` and `fun startBreak(...)` to this same class.

- [ ] **Step 1: Add the domain models**

Create `shared/src/commonMain/kotlin/com/rizz/focusbox/feature/timer/TimerModels.kt`:

```kotlin
package com.rizz.focusbox.feature.timer

enum class SessionType { FOCUS, SHORT_BREAK, LONG_BREAK }

data class TimerConfig(
    val focusSec: Int,
    val shortBreakSec: Int,
    val longBreakSec: Int,
    val longBreakInterval: Int
) {
    companion object {
        val DEFAULT = TimerConfig(
            focusSec = 25 * 60,
            shortBreakSec = 5 * 60,
            longBreakSec = 15 * 60,
            longBreakInterval = 4
        )
    }
}

data class ActiveSession(
    val type: SessionType,
    val taskName: String?,
    val totalSec: Int,
    val startedAtEpochMillis: Long
)

sealed interface TimerState {
    data object Idle : TimerState
    data class Running(val session: ActiveSession, val remainingSec: Int) : TimerState
    data class Paused(val session: ActiveSession, val remainingSec: Int) : TimerState
    data class Complete(val session: ActiveSession) : TimerState
}
```

- [ ] **Step 2: Add the expect/actual clock**

Create `shared/src/commonMain/kotlin/com/rizz/focusbox/feature/timer/Clock.kt`:

```kotlin
package com.rizz.focusbox.feature.timer

internal expect fun currentTimeMillis(): Long
```

Create `shared/src/androidMain/kotlin/com/rizz/focusbox/feature/timer/Clock.android.kt`:

```kotlin
package com.rizz.focusbox.feature.timer

internal actual fun currentTimeMillis(): Long = System.currentTimeMillis()
```

Create `shared/src/iosMain/kotlin/com/rizz/focusbox/feature/timer/Clock.ios.kt`:

```kotlin
package com.rizz.focusbox.feature.timer

import platform.Foundation.NSDate

internal actual fun currentTimeMillis(): Long = (NSDate().timeIntervalSince1970 * 1000).toLong()
```

- [ ] **Step 3: Write the failing engine tests (start/tick/pause/resume)**

Create `shared/src/commonTest/kotlin/com/rizz/focusbox/feature/timer/TimerEngineTest.kt`:

```kotlin
package com.rizz.focusbox.feature.timer

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertTrue

private class FakeSessionRepository : SessionRepository {
    val inserted = mutableListOf<FocusSessionRecord>()
    override fun insertSession(session: FocusSessionRecord) {
        inserted.add(session)
    }
}

class TimerEngineTest {

    @Test
    fun start_transitions_idle_to_running_focus_session() {
        val engine = TimerEngine(FakeSessionRepository())

        engine.start(
            taskName = "Write report",
            config = TimerConfig(focusSec = 60, shortBreakSec = 10, longBreakSec = 20, longBreakInterval = 4)
        )

        val state = assertIs<TimerState.Running>(engine.state.value)
        assertEquals(SessionType.FOCUS, state.session.type)
        assertEquals("Write report", state.session.taskName)
        assertEquals(60, state.remainingSec)
    }

    @Test
    fun start_while_running_throws() {
        val engine = TimerEngine(FakeSessionRepository())
        engine.start(taskName = null, config = TimerConfig(60, 10, 20, 4))

        assertFailsWith<IllegalStateException> {
            engine.start(taskName = null, config = TimerConfig(60, 10, 20, 4))
        }
    }

    @Test
    fun tick_decrements_remaining_seconds() {
        val engine = TimerEngine(FakeSessionRepository())
        engine.start(taskName = null, config = TimerConfig(focusSec = 3, shortBreakSec = 10, longBreakSec = 20, longBreakInterval = 4))

        engine.tick()

        val state = assertIs<TimerState.Running>(engine.state.value)
        assertEquals(2, state.remainingSec)
    }

    @Test
    fun tick_to_zero_on_focus_session_completes_and_persists() {
        val repository = FakeSessionRepository()
        val engine = TimerEngine(repository)
        engine.start(taskName = "Write report", config = TimerConfig(focusSec = 2, shortBreakSec = 10, longBreakSec = 20, longBreakInterval = 4))

        engine.tick()
        engine.tick()

        assertIs<TimerState.Complete>(engine.state.value)
        assertEquals(1, repository.inserted.size)
        val record = repository.inserted.single()
        assertEquals("FOCUS", record.type)
        assertTrue(record.completed)
        assertEquals(2L, record.actualDurationSec)
    }

    @Test
    fun pause_then_resume_preserves_remaining_seconds() {
        val engine = TimerEngine(FakeSessionRepository())
        engine.start(taskName = null, config = TimerConfig(focusSec = 10, shortBreakSec = 10, longBreakSec = 20, longBreakInterval = 4))
        engine.tick()

        engine.pause()
        assertIs<TimerState.Paused>(engine.state.value)

        engine.resume()
        val state = assertIs<TimerState.Running>(engine.state.value)
        assertEquals(9, state.remainingSec)
    }
}
```

- [ ] **Step 4: Run the tests to verify they fail**

Run: `./gradlew :shared:testAndroidHostTest --tests "com.rizz.focusbox.feature.timer.TimerEngineTest"`
Expected: FAIL to compile — `TimerEngine` is not defined yet.

- [ ] **Step 5: Implement TimerEngine (start/pause/resume/tick)**

Create `shared/src/commonMain/kotlin/com/rizz/focusbox/feature/timer/TimerEngine.kt`:

```kotlin
package com.rizz.focusbox.feature.timer

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.annotation.Single

@Single
class TimerEngine(private val repository: SessionRepository) {

    private val _state = MutableStateFlow<TimerState>(TimerState.Idle)
    val state: StateFlow<TimerState> = _state.asStateFlow()

    private var completedFocusCount = 0

    fun start(taskName: String?, config: TimerConfig = TimerConfig.DEFAULT) {
        val current = _state.value
        check(current is TimerState.Idle || current is TimerState.Complete) {
            "start() only valid from Idle or Complete, was $current"
        }
        val session = ActiveSession(SessionType.FOCUS, taskName, config.focusSec, currentTimeMillis())
        _state.value = TimerState.Running(session, session.totalSec)
    }

    fun pause() {
        val current = _state.value
        check(current is TimerState.Running) { "pause() only valid from Running, was $current" }
        _state.value = TimerState.Paused(current.session, current.remainingSec)
    }

    fun resume() {
        val current = _state.value
        check(current is TimerState.Paused) { "resume() only valid from Paused, was $current" }
        _state.value = TimerState.Running(current.session, current.remainingSec)
    }

    fun tick() {
        val current = _state.value
        if (current !is TimerState.Running) return
        val remaining = current.remainingSec - 1
        if (remaining > 0) {
            _state.value = TimerState.Running(current.session, remaining)
            return
        }
        persistSession(current.session, actualDurationSec = current.session.totalSec, completed = true)
        _state.value = if (current.session.type == SessionType.FOCUS) {
            completedFocusCount++
            TimerState.Complete(current.session)
        } else {
            TimerState.Idle
        }
    }

    private fun persistSession(session: ActiveSession, actualDurationSec: Int, completed: Boolean) {
        repository.insertSession(
            FocusSessionRecord(
                taskName = session.taskName,
                type = session.type.name,
                plannedDurationSec = session.totalSec.toLong(),
                actualDurationSec = actualDurationSec.toLong(),
                startedAt = session.startedAtEpochMillis,
                endedAt = currentTimeMillis(),
                completed = completed
            )
        )
    }
}
```

- [ ] **Step 6: Run the tests to verify they pass**

Run: `./gradlew :shared:testAndroidHostTest --tests "com.rizz.focusbox.feature.timer.TimerEngineTest"`
Expected: all 5 tests PASS.

- [ ] **Step 7: Commit**

```bash
git add shared/src/commonMain/kotlin/com/rizz/focusbox/feature/timer/TimerModels.kt \
        shared/src/commonMain/kotlin/com/rizz/focusbox/feature/timer/Clock.kt \
        shared/src/androidMain/kotlin/com/rizz/focusbox/feature/timer/Clock.android.kt \
        shared/src/iosMain/kotlin/com/rizz/focusbox/feature/timer/Clock.ios.kt \
        shared/src/commonMain/kotlin/com/rizz/focusbox/feature/timer/TimerEngine.kt \
        shared/src/commonTest/kotlin/com/rizz/focusbox/feature/timer/TimerEngineTest.kt
git commit -m "feat: add TimerEngine with start/pause/resume/tick"
```

---

### Task 3: Skip, break cycling, and full build verification

**Files:**
- Modify: `shared/src/commonMain/kotlin/com/rizz/focusbox/feature/timer/TimerEngine.kt`
- Modify: `shared/src/commonTest/kotlin/com/rizz/focusbox/feature/timer/TimerEngineTest.kt`

**Interfaces:**
- Consumes: everything from Tasks 1-2.
- Produces: `TimerEngine.skip()` and `TimerEngine.startBreak(config: TimerConfig = TimerConfig.DEFAULT)`, completing the engine's public API as listed in "Global Design Notes". No later task depends on this one.

- [ ] **Step 1: Write the failing tests for skip and break cycling**

Append these test functions inside the `TimerEngineTest` class in `shared/src/commonTest/kotlin/com/rizz/focusbox/feature/timer/TimerEngineTest.kt` (before the closing `}` of the class):

```kotlin
    @Test
    fun skip_persists_incomplete_session_and_returns_to_idle() {
        val repository = FakeSessionRepository()
        val engine = TimerEngine(repository)
        engine.start(taskName = "Write report", config = TimerConfig(focusSec = 10, shortBreakSec = 10, longBreakSec = 20, longBreakInterval = 4))
        engine.tick()
        engine.tick()

        engine.skip()

        assertEquals(TimerState.Idle, engine.state.value)
        val record = repository.inserted.single()
        assertEquals(false, record.completed)
        assertEquals(2L, record.actualDurationSec)
    }

    @Test
    fun skip_while_idle_throws() {
        val engine = TimerEngine(FakeSessionRepository())
        assertFailsWith<IllegalStateException> { engine.skip() }
    }

    @Test
    fun start_break_after_first_focus_session_picks_short_break() {
        val engine = TimerEngine(FakeSessionRepository())
        val config = TimerConfig(focusSec = 1, shortBreakSec = 10, longBreakSec = 20, longBreakInterval = 4)
        engine.start(taskName = null, config = config)
        engine.tick()

        engine.startBreak(config)

        val state = assertIs<TimerState.Running>(engine.state.value)
        assertEquals(SessionType.SHORT_BREAK, state.session.type)
        assertEquals(10, state.remainingSec)
    }

    @Test
    fun start_break_after_fourth_focus_session_picks_long_break() {
        val engine = TimerEngine(FakeSessionRepository())
        val config = TimerConfig(focusSec = 1, shortBreakSec = 10, longBreakSec = 20, longBreakInterval = 4)
        repeat(3) {
            engine.start(taskName = null, config = config)
            engine.tick()
            engine.startBreak(config)
            engine.tick()
        }
        engine.start(taskName = null, config = config)
        engine.tick()

        engine.startBreak(config)

        val state = assertIs<TimerState.Running>(engine.state.value)
        assertEquals(SessionType.LONG_BREAK, state.session.type)
    }

    @Test
    fun tick_to_zero_on_break_session_returns_to_idle() {
        val repository = FakeSessionRepository()
        val engine = TimerEngine(repository)
        val config = TimerConfig(focusSec = 1, shortBreakSec = 2, longBreakSec = 20, longBreakInterval = 4)
        engine.start(taskName = null, config = config)
        engine.tick()
        engine.startBreak(config)

        engine.tick()
        engine.tick()

        assertEquals(TimerState.Idle, engine.state.value)
        assertEquals(2, repository.inserted.size)
        assertEquals("SHORT_BREAK", repository.inserted.last().type)
        assertTrue(repository.inserted.last().completed)
    }

    @Test
    fun start_break_while_running_throws() {
        val engine = TimerEngine(FakeSessionRepository())
        engine.start(taskName = null, config = TimerConfig(10, 10, 20, 4))
        assertFailsWith<IllegalStateException> { engine.startBreak() }
    }
```

- [ ] **Step 2: Run the tests to verify they fail**

Run: `./gradlew :shared:testAndroidHostTest --tests "com.rizz.focusbox.feature.timer.TimerEngineTest"`
Expected: FAIL to compile — `skip()` and `startBreak()` are not defined yet.

- [ ] **Step 3: Implement skip() and startBreak()**

In `shared/src/commonMain/kotlin/com/rizz/focusbox/feature/timer/TimerEngine.kt`, add these two functions to the `TimerEngine` class (e.g. after `tick()` and before `persistSession`):

```kotlin
    fun skip() {
        val current = _state.value
        val (session, remaining) = when (current) {
            is TimerState.Running -> current.session to current.remainingSec
            is TimerState.Paused -> current.session to current.remainingSec
            else -> error("skip() only valid from Running or Paused, was $current")
        }
        val actual = session.totalSec - remaining
        persistSession(session, actualDurationSec = actual, completed = false)
        _state.value = TimerState.Idle
    }

    fun startBreak(config: TimerConfig = TimerConfig.DEFAULT) {
        val current = _state.value
        check(current is TimerState.Complete) { "startBreak() only valid from Complete, was $current" }
        val isLong = completedFocusCount % config.longBreakInterval == 0
        val type = if (isLong) SessionType.LONG_BREAK else SessionType.SHORT_BREAK
        val totalSec = if (isLong) config.longBreakSec else config.shortBreakSec
        val session = ActiveSession(type, current.session.taskName, totalSec, currentTimeMillis())
        _state.value = TimerState.Running(session, totalSec)
    }
```

- [ ] **Step 4: Run the tests to verify they pass**

Run: `./gradlew :shared:testAndroidHostTest --tests "com.rizz.focusbox.feature.timer.TimerEngineTest"`
Expected: all 10 tests PASS.

- [ ] **Step 5: Full multi-target build verification**

Run each of these and confirm all succeed (per `AGENTS.md`'s green-build status, all three must stay green):

```bash
./gradlew :shared:testAndroidHostTest
./gradlew :shared:iosSimulatorArm64Test
./gradlew :androidApp:assembleDebug
```

Expected: all three BUILD SUCCESSFUL, with `TimerEngineTest` (10 tests) and `SqlDelightSessionRepositoryTest` (2 tests) passing.

- [ ] **Step 6: Commit**

```bash
git add shared/src/commonMain/kotlin/com/rizz/focusbox/feature/timer/TimerEngine.kt \
        shared/src/commonTest/kotlin/com/rizz/focusbox/feature/timer/TimerEngineTest.kt
git commit -m "feat: add skip and break cycling to TimerEngine"
```
