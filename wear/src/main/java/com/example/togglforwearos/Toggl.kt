package com.example.togglforwearos

import android.graphics.Color
import org.json.JSONObject
import java.time.Duration

//data class UserInfo(var timeEntries: List<TimeEntry>,
//                    var projects: List<Project>)
//
//data class Project(var id: String, var name: String, var color: Color)
//
//data class TimeEntry(var startTimestamp:java.time.Instant,
//                     var stopTimestamp:java.time.Instant,
//                     var duration: java.time.Duration,
//                     var project:Project)

class UserInfo(val json: JSONObject){
    var projectsMap: MutableMap<String, Project> = mutableMapOf()
    var projects: MutableList<Project> = mutableListOf()
    var timeEntries: MutableList<TimeEntry> = mutableListOf()
    init {
        val projects_array = json.getJSONObject("data").getJSONArray("projects")
        for (i in 0..(projects_array.length()-1)){
            val project = projects_array.getJSONObject(i)
            projectsMap.put(project.getString("id"), Project(project))
            projects.add(Project(project))
        }
        val timeEntriesArray = json.getJSONObject("data").getJSONArray("time_entries")
        for (i in 0..(timeEntriesArray.length()-1)){
            val timeEntry = timeEntriesArray.getJSONObject(i)
            var timeEntryColor = Color.BLACK
            if(timeEntry.has("pid")){
                timeEntryColor = projectsMap[timeEntry.getString("pid")]!!.color
            }
            timeEntries.add(TimeEntry(timeEntry, timeEntryColor))
        }
    }


}

class Project(val json: JSONObject){
    val name = json.getString("name")
    val color: Int = Color.parseColor(json.getString("hex_color"))
}

class TimeEntry(val json: JSONObject, val projectColor: Int): Comparable<TimeEntry>{
    var durationSeconds: Long = json.getString("duration").toLong()
    var startTimeEpoch: Long = convertStringToInstant(json.getString("start")).epochSecond
    val isCurrentlyRunning: Boolean = !json.has("stop")
    var endTimeEpoch: Long? = null
    init {
        if(!isCurrentlyRunning){
            endTimeEpoch = startTimeEpoch + durationSeconds
        }
    }


    override operator fun compareTo(other: TimeEntry): Int {
        if (this.startTimeEpoch > other.startTimeEpoch) return 1
        if (this.startTimeEpoch < other.startTimeEpoch) return -1
        return 0
    }
}

