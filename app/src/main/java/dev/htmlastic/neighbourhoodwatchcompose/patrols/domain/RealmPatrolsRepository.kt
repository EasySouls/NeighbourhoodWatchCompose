package dev.htmlastic.neighbourhoodwatchcompose.patrols.domain

import dev.htmlastic.neighbourhoodwatchcompose.core.data.models.CivilGuard
import dev.htmlastic.neighbourhoodwatchcompose.patrols.data.Patrol
import dev.htmlastic.neighbourhoodwatchcompose.patrols.data.PatrolState
import dev.htmlastic.neighbourhoodwatchcompose.patrols.data.PatrolType
import dev.htmlastic.neighbourhoodwatchcompose.patrols.data.PatrolsRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.internal.interop.asRealmObjectIdT
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId
import javax.inject.Inject

/**
 * An implementation of [PatrolsRepository] using Realm.
 *
 * @param realm The Realm instance to use.
 */
class RealmPatrolsRepository @Inject constructor(
    var realm: Realm
): PatrolsRepository {
    override fun getOngoingParticipatedPatrol(civilGuard: CivilGuard): Flow<Patrol?> {
        val ongoingPatrols = realm.query<Patrol>("patrolState.state == $0", PatrolType.ONGOING.state).find()
        for (patrol in ongoingPatrols) {
            if (patrol.participants.contains(civilGuard)) {
                return realm.query<Patrol>("_id == $patrol._id").asFlow().map { it.list.firstOrNull() }
            }
        }
        return emptyFlow()
    }

    override fun getOngoingPatrolsByDepartment(departmentId: ObjectId): Flow<List<Patrol>> {
        return realm.query<Patrol>("department._id == $0 AND patrolState.state == $1", departmentId, PatrolType.ONGOING.state)
            .asFlow()
            .map { it.list }
    }
}