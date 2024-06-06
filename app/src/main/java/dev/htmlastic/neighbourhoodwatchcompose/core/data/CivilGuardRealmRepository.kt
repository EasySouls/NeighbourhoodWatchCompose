package dev.htmlastic.neighbourhoodwatchcompose.core.data

import dev.htmlastic.neighbourhoodwatchcompose.core.data.RealmRepository
import dev.htmlastic.neighbourhoodwatchcompose.core.data.models.CivilGuard
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId

interface CivilGuardRealmRepository: RealmRepository {
    fun getCivilGuards(): Flow<List<CivilGuard>>
    fun filterCivilGuards(query: String): Flow<List<CivilGuard>>
    suspend fun insertCivilGuard(civilGuard: CivilGuard)
    suspend fun updateCivilGuard(civilGuard: CivilGuard)
    suspend fun deleteCivilGuard(id: ObjectId)

}