package dev.htmlastic.neighbourhoodwatchcompose

import kotlinx.serialization.Serializable

sealed class Route {

    @Serializable
    data object PatrolsScreen : Route()

    @Serializable
    data object ProfileScreen : Route()

    @Serializable
    data object MessagesScreen : Route()

    @Serializable
    data object CalendarScreen : Route()
}