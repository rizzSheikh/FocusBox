package com.rizz.focusbox.data.stats

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.rizz.focusbox.data.dao.FocusSessionDao
import com.rizz.focusbox.db.FocusSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single(binds = [StatsRepository::class])
class SqlDelightStatsRepository(
    private val focusSessionDao: FocusSessionDao,
) : StatsRepository {
    override fun observeSessions(): Flow<List<FocusSession>> =
        focusSessionDao.selectAll().asFlow().mapToList(Dispatchers.Default)
}
