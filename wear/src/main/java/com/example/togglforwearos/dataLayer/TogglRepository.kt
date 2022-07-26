package com.example.togglforwearos.dataLayer

import android.content.Context
import android.util.Base64
import android.util.Log
import com.example.togglforwearos.*
import kotlinx.coroutines.*
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.lang.Exception
import java.lang.NullPointerException
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets


const val USER_INFO_KEY = "user info key"

/**
 * Handles all Toggl data
 */
class TogglRepository(val context: Context) {

    private val apiToken = APITokenRepository(context).togglAPIToken
    val togglWebApi = TogglWebApi(apiToken)
    var userInfo: UserInfo? = getUserInfoFromStorage()


    fun getStringFromStorage(key: String): String? {
        return getSharedPref(context).getString(key, null)
    }


    fun putStringToStorage(key: String, string: String) {
        with(getSharedPref(context).edit()) {
            putString(USER_INFO_KEY, string)
            apply()
        }
    }


    fun getUserInfoFromStorage(): UserInfo? {
        val userInfoString: String? = getStringFromStorage(USER_INFO_KEY)
        return if (userInfoString != null) {
            UserInfo(JSONObject(userInfoString))
        } else {
            null
        }
    }


    /** Updates [userInfo] to match the information from  */
    suspend fun updateUserInfo() {
        val newUserInfoJSONObject = togglWebApi.getUserInfo()
        userInfo = UserInfo(newUserInfoJSONObject!!)
        putStringToStorage(USER_INFO_KEY, userInfo!!.json.toString())
    }


    /**
     * Returns [TimeEntry] representing the running timer.
     * If there are no timers running returns null
     * */
    suspend fun getRunningTimer(): TimeEntry {
        val json = togglWebApi.getRunningTimer().getJSONObject("data")
        return TimeEntry(json, userInfo!!.getProjectByPid(json.getString("pid")))
    }


    suspend fun getUserProjects(): List<Project> {
        TODO("Not yet implemented")
    }


    fun start_new_entry() {
        TODO("Not yet implemented")
    }


    fun sync_everything() {
        TODO("Not yet implemented")
    }
}


