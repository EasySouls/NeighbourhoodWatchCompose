package dev.htmlastic.neighbourhoodwatchcompose.patrols.data

import dev.htmlastic.neighbourhoodwatchcompose.core.data.models.CivilGuard
import dev.htmlastic.neighbourhoodwatchcompose.core.data.models.Department
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId

interface PatrolsRepository {
    /**
     *  Gets the current ongoing patrol of a civil guard, if any
     *
     * @param civilGuard The civil guard
     * @return The current patrol or null
     */
    fun getOngoingParticipatedPatrol(civilGuard: CivilGuard): Flow<Patrol?>

    /**
     *  Get all ongoing patrols by department
     *
     *  @param departmentId The id of the department
     *  @return A flow of a list of patrols
     */
    fun getOngoingPatrolsByDepartment(departmentId: ObjectId): Flow<List<Patrol>>
}