package dev.htmlastic.neighbourhoodwatchcompose.core.data.models

import io.realm.kotlin.ext.realmSetOf
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmSet
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Department : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var name: String = ""
    var civilGuards: RealmSet<CivilGuard> = realmSetOf()
    var createdAt: RealmInstant = RealmInstant.now()
}