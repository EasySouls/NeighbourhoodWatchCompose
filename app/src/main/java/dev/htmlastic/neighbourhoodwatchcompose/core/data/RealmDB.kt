package dev.htmlastic.neighbourhoodwatchcompose.core.data

import android.util.Log
import dev.htmlastic.neighbourhoodwatchcompose.core.data.Constants.APP_ID
import dev.htmlastic.neighbourhoodwatchcompose.core.data.models.CivilGuard
import dev.htmlastic.neighbourhoodwatchcompose.core.data.models.Department
import dev.htmlastic.neighbourhoodwatchcompose.patrols.data.Patrol
import io.realm.kotlin.Configuration
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId

object RealmDB: CivilGuardRealmRepository {
    private val app = App.create(APP_ID)
    private val user = app.currentUser
    private lateinit var realm: Realm

    private const val SYNC_REALM = true

    init {
        configureTheRealm()
    }
    override fun configureTheRealm() {
        if (user != null) {
            val config: Configuration = if (SYNC_REALM) {
                SyncConfiguration.Builder(
                    user = user,
                    schema = setOf(CivilGuard::class, Patrol::class)
                )
                    .initialSubscriptions { sub ->
                        add(query = sub.query<CivilGuard>(query = "owner_id == $0", user.id))
                    }
                    .compactOnLaunch()
                    .build()
            } else {
                RealmConfiguration.Builder(
                    schema = setOf(CivilGuard::class, Patrol::class)
                )
                    .compactOnLaunch()
                    .build()
            }
            realm = Realm.open(config)
        }
    }
    override fun getCivilGuards(): Flow<List<CivilGuard>> {
        return realm.query<CivilGuard>().asFlow().map { it.list }
    }

    override fun getCivilGuardById(id: ObjectId): Flow<CivilGuard?> {
        return realm.query<CivilGuard>(query = "_id == $0", id)
            .asFlow().map { it.list.firstOrNull() }
    }

    override fun getCivilGuardsByDepartment(department: Department): Flow<List<CivilGuard>> {
        return realm.query<CivilGuard>(query = "department == $0", department)
            .asFlow().map { it.list }
    }

    override fun filterCivilGuards(query: String): Flow<List<CivilGuard>> {
        return realm.query<CivilGuard>(query = "name CONTAINS[c] $0", query)
            .asFlow().map { it.list }
    }


    override suspend fun insertCivilGuard(civilGuard: CivilGuard, department: Department) {
        if (user != null) {
            realm.write {
                try {
                    val queriedDepartment = query<Department>(query = "_id == $0", department._id)
                        .first()
                        .find()

                    if (queriedDepartment == null) {
                        Log.d("RealmDB", "insertCivilGuard: Queried Department doesn't exist: $department")
                        return@write
                    }

                    // TODO: Check if this adds the Civil Guard to the Department as well
                    copyToRealm(civilGuard.apply {
                        owner_id = user.id
                        this.department = department
                    } )
                } catch (e: Exception) {
                    Log.d("RealmDB", "insertCivilGuard: ${e.message}")
                }
            }
        }
    }

    override suspend fun updateCivilGuard(civilGuard: CivilGuard) {
        realm.write {
            var queriedCivilGuard = query<CivilGuard>(query = "id == $0", civilGuard._id)
                .first()
                .find()
            if (queriedCivilGuard != null) {
                // TODO: Check if this works with the reassignment
                // Maybe only works with member assignment or by calling copyToRealm
                queriedCivilGuard = civilGuard
            } else {
                Log.d("RealmDB", "updateCivilGuard: Queries Civil Guard doesn't exist")
            }
        }
    }

    override suspend fun deleteCivilGuard(id: ObjectId) {
        realm.write {
            try {
                val civilGuard = query<CivilGuard>(query = "id == $0", id)
                    .first()
                    .find()
                civilGuard?.let { delete(it) }
            } catch (e: Exception) {
                Log.d("RealmDB", "deleteCivilGuard: ${e.message}")
            }
        }
    }
}