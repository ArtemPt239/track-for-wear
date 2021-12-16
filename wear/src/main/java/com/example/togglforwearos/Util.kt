package com.example.togglforwearos

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import java.time.Instant
import java.time.OffsetDateTime

fun convertStringToInstant(string: String): Instant {
    return OffsetDateTime.parse(string).toInstant()
}

fun getSharedPref(applicationContext : Context): SharedPreferences {
    return PreferenceManager.getDefaultSharedPreferences(
        applicationContext
    )!!
}