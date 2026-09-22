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
