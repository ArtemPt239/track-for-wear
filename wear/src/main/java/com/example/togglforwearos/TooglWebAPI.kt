package com.example.togglforwearos

import android.util.Base64
import java.io.InputStream
import java.net.URL
import java.net.URLConnection

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import org.json.JSONException
import java.io.BufferedReader
import java.io.IOException
import java.lang.StringBuilder


const val BASE_API_URL = "https://api.track.toggl.com/api/v8/"


class TooglWebAPI(val apiToken: String?) {

    // get User's projects and recent time entries
    suspend fun getUserData(): JSONObject? {
        // Connecting to toggl API
        return withContext(Dispatchers.IO) {
            return@withContext fetchJSON(URL(BASE_API_URL + "me"))
        }
    }


    // get User's projects and recent time entries
    suspend fun getCurrentTimeEntry(): JSONObject? {
        return withContext(Dispatchers.IO) {
            return@withContext fetchJSON(URL(BASE_API_URL + "time_entries/current"))
        }
    }


    // Auth to Toggl Track api and fetch a json
    private fun fetchJSON (url: URL): JSONObject?{
        val urlConnection: URLConnection = url.openConnection()
        urlConnection.setRequestProperty(
            "Authorization",
            "Basic " + Base64.encodeToString(
                "$apiToken:api_token".toByteArray(StandardCharsets.UTF_8),
                Base64.DEFAULT
            )
        )
        val inputStream: InputStream = urlConnection.getInputStream()
        try {
            val streamReader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
            val responseStrBuilder = StringBuilder()
            var inputStr: String?
            while (streamReader.readLine()
                    .also { inputStr = it } != null
            ) responseStrBuilder.append(inputStr)

            //returns the json object
            return JSONObject(responseStrBuilder.toString())
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        //if something went wrong, return null
        return null
    }
}