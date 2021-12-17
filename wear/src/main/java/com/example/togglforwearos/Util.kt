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

fun durationSecondsToString(durationSeconds:Long): String{
    val hours = durationSeconds / 3600;
    val minutes = (durationSeconds % 3600) / 60;
    val seconds = durationSeconds % 60;

    return String.format("%d:%02d:%02d", hours, minutes, seconds);
}