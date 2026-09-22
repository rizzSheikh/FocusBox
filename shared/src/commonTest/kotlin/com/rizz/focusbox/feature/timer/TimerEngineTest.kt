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
