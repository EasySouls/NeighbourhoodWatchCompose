package dev.htmlastic.neighbourhoodwatchcompose.patrols.data

import dev.htmlastic.neighbourhoodwatchcompose.data.core.CivilGuard
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Patrol(): RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var startedAt: RealmInstant = RealmInstant.now()
    var endedAt: RealmInstant? = null
    var patrolType: PatrolType? = PatrolType.STARTED
    var participants: List<CivilGuard> = emptyList()
}