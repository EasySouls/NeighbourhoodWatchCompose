package dev.htmlastic.neighbourhoodwatchcompose.patrols.data

import dev.htmlastic.neighbourhoodwatchcompose.core.data.models.Department
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId

interface PatrolsRepository {
    /**
     *  Gets the current ongoing patrol of the user, if any
     *
     * @param userId The id of the user
     * @return The current patrol or null
     */
    fun getOngoingParticipatedPatrol(userId: ObjectId): Patrol?

    /**
     *  Get all ongoing patrols by department
     *
     *  @param departmentId The id of the department
     *  @return A flow of a list of patrols
     */
    fun getOngoingPatrolsByDepartment(departmentId: ObjectId): Flow<List<Patrol>>
}