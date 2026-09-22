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
