package dev.htmlastic.neighbourhoodwatchcompose.core.data

import android.util.Log
import dev.htmlastic.neighbourhoodwatchcompose.core.data.models.Event
import dev.htmlastic.neighbourhoodwatchcompose.core.domain.EventRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId
import javax.inject.Inject

class RealmEventRepository @Inject constructor(
    private val realm: Realm
) : EventRepository {
    override fun getAllEvents(): Flow<List<Event>> {
        return realm.query<Event>()
            .asFlow()
            .map { it.list }
    }

    override fun getEventsByDepartment(departmentId: ObjectId): Flow<List<Event>> {
        return realm.query<Event>("department._id = $0", departmentId)
            .asFlow()
            .map { it.list }
    }

    override fun getUpcomingEventsByDepartment(departmentId: ObjectId): Flow<List<Event>> {
        return realm.query<Event>(
            "department._id = $0 AND date > $1",
            departmentId,
            System.currentTimeMillis()
        )
            .asFlow()
            .map { it.list }
    }

    override suspend fun addEvent(event: Event) {
        realm.write {
            try {
                copyToRealm(event)
            } catch (e: Exception) {
                Log.e("RealmEventRepository", "Failed to add event: $event. Error: ${e.message}")
            }
        }
    }

    override suspend fun updateEvent(event: Event) {
        realm.write {
            val existingEvent = query<Event>("_id = $0", event._id).first().find()

            if (existingEvent == null) {
                Log.e("RealmEventRepository", "Failed to update event: $event. Event not found.")
                return@write
            }

            existingEvent.name = event.name
            existingEvent.description = event.description
            existingEvent.date = event.date
            existingEvent.department = event.department
            // copyToRealm(existingEvent)
        }
    }
}