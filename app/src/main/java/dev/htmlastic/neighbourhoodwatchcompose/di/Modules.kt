package dev.htmlastic.neighbourhoodwatchcompose.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.htmlastic.neighbourhoodwatchcompose.core.data.Constants.APP_ID
import dev.htmlastic.neighbourhoodwatchcompose.core.data.RealmEventRepository
import dev.htmlastic.neighbourhoodwatchcompose.core.data.models.CivilGuard
import dev.htmlastic.neighbourhoodwatchcompose.core.data.models.Department
import dev.htmlastic.neighbourhoodwatchcompose.core.data.models.Event
import dev.htmlastic.neighbourhoodwatchcompose.core.domain.EventRepository
import dev.htmlastic.neighbourhoodwatchcompose.patrols.data.Patrol
import dev.htmlastic.neighbourhoodwatchcompose.patrols.data.PatrolState
import dev.htmlastic.neighbourhoodwatchcompose.patrols.data.PatrolsRepository
import dev.htmlastic.neighbourhoodwatchcompose.patrols.domain.RealmPatrolsRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RealmModule {
    @Provides
    @Singleton
    fun provideRealm(): Realm {
        val schema = setOf(
            CivilGuard::class,
            Patrol::class,
            Department::class,
            PatrolState::class,
            Event::class
        )

        val app = App.create(APP_ID)
        val user = app.currentUser
            ?: return Realm.open(
                RealmConfiguration.Builder(
                    schema = schema
                )
                    .deleteRealmIfMigrationNeeded()
                    .build()
            )

        // If there is a logged in user, connect to the synced Realm
        val config = SyncConfiguration.Builder(
            user = user,
            schema = schema
        )
            .initialSubscriptions { sub ->
                add(query = sub.query<CivilGuard>(query = "owner_id == $0", user.id))
            }
            .compactOnLaunch()
            .build()
        return Realm.open(config)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPatrolsRepository(
        patrolsRepository: RealmPatrolsRepository
    ): PatrolsRepository

    @Binds
    @Singleton
    abstract fun bindPatrolState(
        eventRepository: RealmEventRepository
    ): EventRepository

}