package dev.htmlastic.neighbourhoodwatchcompose.patrols.domain

import dev.htmlastic.neighbourhoodwatchcompose.patrols.data.Patrol
import dev.htmlastic.neighbourhoodwatchcompose.patrols.data.PatrolsRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.asFlow
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
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
    override fun getOngoingParticipatedPatrol(userId: ObjectId): Flow<Patrol?> {
        // TODO: Return only the ongoing patrol and search in the participants
        return realm.query<Patrol>("$userId in participants")
            .asFlow()
            .map { it.list.first() }
    }

    override fun getOngoingPatrolsByDepartment(departmentId: ObjectId): Flow<List<Patrol>> {
        return realm.query<Patrol>("departmentId == $departmentId and patrolType == ONGOING")
            .asFlow()
            .map { it.list }
    }
}