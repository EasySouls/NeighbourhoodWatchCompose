package dev.htmlastic.neighbourhoodwatchcompose.patrols.data

import io.realm.kotlin.types.RealmObject

enum class PatrolType(var state: String) {
    ONGOING("Ongoing"),
    STOPPED("Stopped")
}

class PatrolState: RealmObject {
    var name: String? = null
    private var state: String = PatrolType.ONGOING.state
    var patrolType: PatrolType
        get() = PatrolType.valueOf(state)
        set(value) {
            state = value.state
        }
}