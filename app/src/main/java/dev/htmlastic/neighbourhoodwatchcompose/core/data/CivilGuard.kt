package dev.htmlastic.neighbourhoodwatchcompose.core.data

import dev.htmlastic.neighbourhoodwatchcompose.patrols.data.Patrol
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class CivilGuard: RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var name: String = ""
    var phoneNumber: String = ""
    var activePatrols: List<Patrol> = emptyList()
}