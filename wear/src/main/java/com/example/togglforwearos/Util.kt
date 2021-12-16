package com.example.togglforwearos

import java.time.Instant
import java.time.OffsetDateTime

fun convertStringToInstant(string: String): Instant {
    return OffsetDateTime.parse(string).toInstant()
}