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
        // shortBreakSec = 1 (not 10 as in the brief's verbatim text): the loop below calls tick()
        // exactly once per break to return the engine to Idle before the next start(). With
        // shortBreakSec = 10, that single tick only decrements the break from 10 to 9 (still
        // Running), so the next start() throws IllegalStateException. This test only asserts the
        // 4th break's SessionType, not its duration, so shortBreakSec = 1 preserves the test's
        // intent while making the loop's single-tick-per-break assumption hold.
        val config = TimerConfig(focusSec = 1, shortBreakSec = 1, longBreakSec = 20, longBreakInterval = 4)
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
}
