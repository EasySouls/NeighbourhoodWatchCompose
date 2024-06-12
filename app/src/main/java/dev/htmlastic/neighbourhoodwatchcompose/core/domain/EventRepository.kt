package dev.htmlastic.neighbourhoodwatchcompose.core.domain

import dev.htmlastic.neighbourhoodwatchcompose.core.data.models.Event
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId

interface EventRepository {

    fun getAllEvents(): Flow<List<Event>>

    fun getEventsByDepartment(departmentId: ObjectId): Flow<List<Event>>

    fun getUpcomingEventsByDepartment(departmentId: ObjectId): Flow<List<Event>>

    suspend fun addEvent(event: Event)

    /**
     * Updates an event in the database.
     *
     * Updates every field in the event, so provide the correct values.
     */
    suspend fun updateEvent(event: Event)
}