package com.ljb.alarmmanager

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

//https://www.devkuma.com/docs/java/convert-between-date-to-localdatetime/
fun LocalDate.toDate() = Date.from(
    this.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()
)
fun LocalDateTime.toDate() = Date.from(
    this.atZone(ZoneId.systemDefault()).toInstant()
)

fun Date.toLocalDate()= Instant
    .ofEpochMilli(this.time)
    .atZone(ZoneId.systemDefault())
    .toLocalDate()

fun Date.toLocalDateTime() = Instant
    .ofEpochMilli(this.time)
    .atZone(ZoneId.systemDefault())
    .toLocalDateTime()