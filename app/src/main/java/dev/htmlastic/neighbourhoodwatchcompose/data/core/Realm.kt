package dev.htmlastic.neighbourhoodwatchcompose.data.core

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

val config = RealmConfiguration.create(schema = setOf())
val realm = Realm.open(config)