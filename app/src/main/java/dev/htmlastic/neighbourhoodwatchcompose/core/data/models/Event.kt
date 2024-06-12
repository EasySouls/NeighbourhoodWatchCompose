package dev.htmlastic.neighbourhoodwatchcompose.core.data.models

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.ext.realmSetOf
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmSet
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Event: RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var name: String = ""
    var description: String = ""
    var participants: RealmSet<CivilGuard> = realmSetOf()
    var department: Department? = null
    var date: RealmInstant = RealmInstant.now()
    var createdAt: RealmInstant = RealmInstant.now()
}