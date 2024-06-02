package dev.htmlastic.neighbourhoodwatchcompose.core.domain

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

object DateTimeUtils {

    fun now(): LocalDateTime {
        return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    }

    fun toEpochMillis(dateTime: LocalDateTime): Long {
        return dateTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }

    fun fromEpochMillis(epochMillis: Long): LocalDateTime {
        return Instant.fromEpochMilliseconds(epochMillis).toLocalDateTime(TimeZone.currentSystemDefault())
    }

    fun toFormattedText(epochMillis: Long): String {
        val inLocalDateTime = fromEpochMillis(epochMillis)
        val hours = if (inLocalDateTime.hour < 10) "0${inLocalDateTime.hour}" else inLocalDateTime.hour
        val minutes = if (inLocalDateTime.minute < 10) "0${inLocalDateTime.minute}" else inLocalDateTime.minute
        val seconds = if (inLocalDateTime.second < 10) "0${inLocalDateTime.second}" else inLocalDateTime.second
        return "${hours}:${minutes}.${seconds}"
    }

    fun format(dateTime: LocalDateTime): String {
        return LocalDateTime.Formats.ISO.format(dateTime)
    }
}