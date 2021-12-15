package com.example.togglforwearos

import android.graphics.Color

data class UserInfo(var timeEntries: List<TimeEntry>,
                    var projects: List<Project>)

data class Project(var id: String, var name: String, var color: Color)

data class TimeEntry(var startTimestamp:java.time.Instant,
                     var stopTimestamp:java.time.Instant,
                     var duration: java.time.Duration,
                     var project:Project)


