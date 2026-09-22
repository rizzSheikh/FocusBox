package com.rizz.focusbox.feature.stats

import com.rizz.focusbox.db.FocusSession
import kotlinx.coroutines.flow.Flow

interface StatsRepository {
    fun observeSessions(): Flow<List<FocusSession>>
}
