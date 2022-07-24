package com.example.togglforwearos

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.widget.Toast
import java.time.Duration
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


fun convertDurationToString(indiff: Duration): String{
    var diff = indiff
    fun getSubstringBetween(string: String, start: String, end: String): String{
        return string.split(start)[1].split(end)[0]
    }
    val days = diff.toDays()
    diff = diff.minusDays(days)
    val hours = diff.toHours()
    diff = diff.minusHours(hours)
    val minutes = diff.toMinutes()
    diff = diff.minusMinutes(minutes)
    var seconds = getSubstringBetween(diff.toString(), "PT", ".")
    if (seconds.length == 1){
        seconds = "0$seconds"
    }
    return if(diff.toHours() == 0L){
        "$minutes:$seconds"
    }else{
        "$hours:$minutes:$seconds"
    }
}


fun durationSecondsToString(durationSeconds:Long): String{
    val hours = durationSeconds / 3600;
    val minutes = (durationSeconds % 3600) / 60;
    val seconds = durationSeconds % 60;

    return String.format("%d:%02d:%02d", hours, minutes, seconds);
}


fun makeMyExceptionMessage(e: java.lang.Exception):String {
    var errorMsg: String? = null
    errorMsg = e.message
    if (errorMsg == null) {
        errorMsg = e.toString()
        if (errorMsg == "") {
            errorMsg = "Unknown Error"
        }
    }
    return errorMsg
}


fun showToast(msg: String, applicationContext: Context) {
    Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
}