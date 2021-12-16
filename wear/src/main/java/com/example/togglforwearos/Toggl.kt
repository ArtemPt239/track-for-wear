package com.example.togglforwearos

import android.graphics.Color
import org.json.JSONObject

//data class UserInfo(var timeEntries: List<TimeEntry>,
//                    var projects: List<Project>)
//
//data class Project(var id: String, var name: String, var color: Color)
//
//data class TimeEntry(var startTimestamp:java.time.Instant,
//                     var stopTimestamp:java.time.Instant,
//                     var duration: java.time.Duration,
//                     var project:Project)

class UserInfo(var json: JSONObject){
    var projectsMap: MutableMap<String, Project> = mutableMapOf()
    var projects: MutableList<Project> = mutableListOf()
    init {
        val projects_array = json.getJSONObject("data").getJSONArray("projects")
        for (i in 0..(projects_array.length()-1)){
            val project = projects_array.getJSONObject(i)
            projectsMap.put(project.getString("id"), Project(project))
            projects.add(Project(project))
        }
    }


}

class Project(var json: JSONObject){
    val name = json.getString("name")
    val color: Int = Color.parseColor(json.getString("hex_color"))
}

