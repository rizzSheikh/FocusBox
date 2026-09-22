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
